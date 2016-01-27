package org.alopex.ragtag.net;

import org.alopex.ragtag.net.packets.NetData;
import org.alopex.ragtag.net.packets.NetRequest;
import org.alopex.ragtag.net.worker.Worker;
import org.alopex.ragtag.net.worker.WorkerManager;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class ServerListener extends Listener {

	public void connected(Connection connection) {
		WorkerManager.registerWorker(new Worker(connection));
	}
	
	public void disconnected(Connection connection) {
		WorkerManager.disconnectWorker(connection);
	}
	
	public void received(Connection connection, Object object) {
		if(object instanceof NetRequest) {
			NetRequest.processRequest(connection, object);
		} else if(object instanceof NetData) {
			NetData.processData(connection, object);
		}
	}
}
