package org.alopex.ragtag.net.packets;

import org.alopex.ragtag.net.worker.Worker;
import org.alopex.ragtag.net.worker.WorkerManager;

import com.esotericsoftware.kryonet.Connection;

/**
 * Class for when ragtag receives Request
 * @author Kevin Cai
 */
public class Request extends Packet {

	public Request(byte type, Object payload) {
		super(type, payload);
	}

	public static final byte HANDSHAKE = 0x01;
	
	public static void processRequest(Connection connection, Object oRequest) {
		Worker worker = WorkerManager.getWorker(connection);
		if(worker != null) {
			//Request tempReq = (Request) oRequest;
			//Some requests handled here
		}
	}
}
