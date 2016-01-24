package org.alopex.ragtag.net.packets;

import org.alopex.ragtag.Utilities;
import org.alopex.ragtag.net.worker.Worker;
import org.alopex.ragtag.net.worker.WorkerManager;

import com.esotericsoftware.kryonet.Connection;

/**
 * Class for when ragtag receives Data
 * @author Kevin Cai
 */
public class NetData extends Packet {
	
	public NetData() { }
	
	public NetData(byte type, Object payload) {
		super(type, payload);
	}

	public static final byte HANDSHAKE = 0x02;
	public static final byte SYSRES    = 0x04;
	public static final byte BENCHMARK = 0x06;
	public static final byte JOB       = 0x08;
	
	public static void processData(Connection connection, Object oData) {
		Worker worker = WorkerManager.getWorker(connection);
		if(worker != null) {
			NetData tempData = (NetData) oData;
			Object oPayload = tempData.getPayload();
			
			switch(tempData.getType()) {
				//Set the worker ID based on handshake
				case HANDSHAKE:
					Utilities.log("DataCore", "Received handshake data", false);
					if(oPayload instanceof String) {
						String handshakeID = (String) tempData.getPayload();
						worker.setID(handshakeID);
					}
					break;
					
				case SYSRES:
					Utilities.log("DataCore", "Received system resource report from " + worker.getID().substring(0, 5), false);
					if(oPayload instanceof Double) {
						double sysLoad = ((Double) oPayload).doubleValue();
						worker.setLoad(sysLoad);
					}
					break;
				
				case BENCHMARK:
					Utilities.log("DataCore", "Received benchmark data", false);
					if(oPayload instanceof Integer) {
						int iterations = ((Integer) oPayload).intValue();
						worker.setBenchmark(iterations);
					}
					break;
					
				case JOB:
					Utilities.log("DataCore", "Received job data from " + worker.getID().substring(0, 5), false);
					WorkerManager.calculateShares();
					if(oPayload instanceof String) {
						String output = (String) oPayload;
						//TODO: do something with output
						Utilities.log("DataCore", "\t" + output, false);
					}
					break;
			}
		}
	}
}
