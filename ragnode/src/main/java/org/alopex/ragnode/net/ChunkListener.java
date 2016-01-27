package org.alopex.ragnode.net;

import java.util.ArrayList;

import org.alopex.ragnode.core.Utilities;
import org.alopex.ragnode.module.Job;
import org.alopex.ragnode.net.packets.NetData;
import org.alopex.ragnode.net.packets.NetRequest;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.util.TcpIdleSender;

public class ChunkListener extends TcpIdleSender {

	private static ArrayList<NetData> sendQueue;

	public ChunkListener() {
		if(sendQueue == null) {
			sendQueue = new ArrayList<NetData> ();
		}
	}

	@Override
	public void idle(Connection connection) {
		if(!sendQueue.isEmpty()) {
			connection.sendTCP(sendQueue.remove(0));
		}
	}

	public void received(final Connection connection, Object object) {
		if(object instanceof NetRequest) {
			NetRequest nr = (NetRequest) object;
			if(nr.getType() == NetRequest.JOB) {
				final Object oPayload = nr.getPayload();
				Utilities.log("RequestCore", "Received request for job", false);
				if(oPayload instanceof Job) {
					(new Thread(new Runnable() {
						public void run() {
							Job job = (Job) oPayload;
							Object[] results = job.execute();
							if(results.length == 2) {
								if(results[0] instanceof Long && results[1] instanceof String) {
									long runTime = ((Long) results[0]).longValue();
									sendQueue.add(new NetData(NetData.BENCHMARK, runTime));
									String output = (String) results[1];
									sendQueue.add(new NetData(NetData.JOB, output));
								}
							}
						}
					})).start();
				}
			}
		}
	}

	@Override
	protected Object next() {
		return null;
	}
}
