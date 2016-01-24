package org.alopex.ragnode.net.packets;

import com.esotericsoftware.kryonet.Connection;

/**
 * Class for when ragtagnode receives Data
 * @author Kevin Cai
 */
public class NetData extends Packet {
	
	public NetData() {}
	
	public NetData(byte type, Object payload) {
		super(type, payload);
	}

	public static final byte HANDSHAKE = 0x02;
	public static final byte SYSRES    = 0x04;
	public static final byte BENCHMARK = 0x06;
	public static final byte JOB       = 0x08;
	
	public static void processData(Connection connection, Object oData) {
		/**
		Worker worker = WorkerManager.getWorker(connection);
		if(worker != null) {
			NetData tempData = (NetData) oData;
			switch(tempData.getType()) {
				//Set the worker ID based on handshake
				case HANDSHAKE:
					Utilities.log("DataCore", "Received handshake data: ", false);
					Object hPayload = tempData.getPayload();
					if(hPayload instanceof String) {
						String handshakeID = (String) tempData.getPayload();
						worker.setID(handshakeID);
					}
					break;
				
				case BENCHMARK:
					Utilities.log("DataCore", "Received benchmark data: ", false);
					Object benchPayload = tempData.getPayload();
					if(benchPayload instanceof Integer) {
						int iterations = ((Integer) benchPayload).intValue();
						worker.setBenchmark(iterations);
					}
					break;
			}
		}
		 */
	}
}
