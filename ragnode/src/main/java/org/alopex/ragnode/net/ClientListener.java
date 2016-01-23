package org.alopex.ragnode.net;

import org.alopex.ragnode.NodeCore;
import org.alopex.ragnode.Utilities;
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
	
	@Override
	public void disconnected(Connection connection) {
		Utilities.log(this, "Connection to server lost, entering backup mode", false);
		(new Thread(new Runnable() {
			public void run() {
				while(!NodeCore.getClientNetworking().initialize()) {
					Utilities.log(this, "Attempting reconnection to server...", false);
					try {
						Thread.sleep(3000);
					} catch(Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		})).start();
	}
}
