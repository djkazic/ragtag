package org.alopex.ragtag.net.packets;

import org.alopex.ragtag.Utilities;
import org.alopex.ragtag.net.worker.Worker;
import org.alopex.ragtag.net.worker.WorkerManager;

import com.esotericsoftware.kryonet.Connection;

/**
 * Class for when ragtag receives Data
 * @author Kevin Cai
 */
public class Data extends Packet {
	
	public static final byte HANDSHAKE = 0x02;
	public static final byte BENCHMARK = 0x04;
	
	public static void processData(Connection connection, Object oData) {
		Worker worker = WorkerManager.getWorker(connection);
		if(worker != null) {
			Data tempData = (Data) oData;
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
	}
}
