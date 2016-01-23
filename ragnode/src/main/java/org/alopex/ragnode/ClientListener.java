package org.alopex.ragnode;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class ClientListener extends Listener {
	
	@Override
	public void connected(Connection connection) {
		Utilities.log(this, "Connected to server, ID" + connection.getID(), false);
	}

	@Override
	public void received(Connection connection, Object obejct) {
		Utilities.log(this, "Received packet on connection ID " + connection.getID(), false);
	}
}
