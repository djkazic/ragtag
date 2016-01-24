package org.alopex.ragnode.net.packets;

import java.util.HashMap;

import org.alopex.ragnode.NodeCore;
import org.alopex.ragnode.SysRes;
import org.alopex.ragnode.Utilities;
import org.alopex.ragnode.module.Job;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;

/**
 * Class for when ragtag receives Request
 * @author Kevin Cai
 */
public class NetRequest extends Packet {
	
	public NetRequest() {}

	public NetRequest(byte type, Object payload) {
		super(type, payload);
	}

	public static final byte HANDSHAKE = 0x01;
	public static final byte SYSRES    = 0x03;
	public static final byte BENCHMARK = 0x05;
	public static final byte JOB       = 0x07;
	
	public static void processRequest(Connection connection, Object oRequest) {
		/**
		Worker worker = WorkerManager.getWorker(connection);
		if(worker != null) {
			//Request tempReq = (Request) oRequest;
			//Some requests handled here
		}
		 */
		Client client = NodeCore.getClientNetworking().getClient();
		NetRequest tempReq = (NetRequest) oRequest;
		Object oPayload = tempReq.getPayload();
		switch(tempReq.getType()) {
			case HANDSHAKE:
				Utilities.log("RequestCore", "Received request for handshake", false);
				client.sendTCP(new NetData(NetData.HANDSHAKE, NodeCore.getId()));
				break;
				
			case SYSRES:
				Utilities.log("RequestCore", "Received request for system resource report", true);
				client.sendTCP(new NetData(NetData.SYSRES, SysRes.load()));
				break;
				
			case JOB:
				Utilities.log("RequestCore", "Received request for job", false);
				if(oPayload instanceof Job) {
					Job job = (Job) oPayload;
					Object[] results = job.execute();
					if(results.length == 2) {
						if(results[0] instanceof Long && results[1] instanceof HashMap<?, ?>) {
							long runTime = ((Long) results[0]).longValue();
							client.sendTCP(new NetData(NetData.BENCHMARK, runTime));
							HashMap<?, ?> output = (HashMap<?, ?>) results[1];
							client.sendTCP(new NetData(NetData.JOB, output));
						}
					}
				}
				break;
		}
	}
}
