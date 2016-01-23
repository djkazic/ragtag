package org.alopex.ragnode;

import org.alopex.ragnode.net.packets.NetData;
import org.alopex.ragnode.net.packets.NetRequest;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class ClientListener extends Listener {
	
	@Override
	public void connected(Connection connection) {
		Utilities.log(this, "Connected to server, ID" + connection.getID(), false);
	}

	@Override
	public void received(Connection connection, Object object) {
		if(object instanceof NetRequest) {
			NetRequest.processRequest(connection, object);
		} else if(object instanceof NetData) {
			NetData.processData(connection, object);
		}
	}
}
