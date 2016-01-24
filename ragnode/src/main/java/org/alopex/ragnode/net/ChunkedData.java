package org.alopex.ragnode.net;

import org.alopex.ragnode.net.packets.NetData;

import com.esotericsoftware.kryonet.Connection;

public class ChunkedData {

	private Connection connection;
	private NetData sendData;
	
	public ChunkedData(Connection connection, NetData sendData) {
		this.connection = connection;
		this.sendData = sendData;
	}
	
	public void send() {
		connection.sendTCP(sendData);
	}
}
