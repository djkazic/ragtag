package org.alopex.ragtag.net.packets;

import org.alopex.ragtag.net.worker.Worker;
import org.alopex.ragtag.net.worker.WorkerManager;

import com.esotericsoftware.kryonet.Connection;

/**
 * Class for when ragtag receives Data
 * @author Kevin Cai
 */
public class Data extends Packet {
	
	public static final byte HANDSHAKE = 0x02;
	
	public static void processData(Connection connection, Object oData) {
		Worker worker = WorkerManager.getWorker(connection);
		Data tempData = (Data) oData;
		switch(tempData.getType()) {
			case HANDSHAKE:
				break;
		}
	}
}
