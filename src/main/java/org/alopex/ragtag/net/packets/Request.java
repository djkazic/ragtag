package org.alopex.ragtag.net.packets;

import org.alopex.ragtag.net.worker.Worker;
import org.alopex.ragtag.net.worker.WorkerManager;

import com.esotericsoftware.kryonet.Connection;

/**
 * Class for when ragtag receives Request
 * @author Kevin Cai
 */
public class Request extends Packet {

	public static final byte HANDSHAKE = 0x01;
	
	public static void processRequest(Connection connection, Object oRequest) {
		Worker worker = WorkerManager.getWorker(connection);
		Request tempReq = (Request) oRequest;
		switch(tempReq.getType()) {
			case HANDSHAKE:
				break;
		}
	}
}
