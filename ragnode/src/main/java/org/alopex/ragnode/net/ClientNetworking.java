package org.alopex.ragnode.net;

import java.net.InetAddress;

import org.alopex.ragnode.Config;
import org.alopex.ragnode.Utilities;
import org.alopex.ragnode.net.packets.NetData;
import org.alopex.ragnode.net.packets.NetRequest;
import org.alopex.ragnode.net.packets.Packet;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;

public class ClientNetworking {

	private Client client;
	
	public void initialize() {
		registerClientListeners();
		startClient();
		connectClient();
	}
	
	private void registerClientListeners() {
		try {
			client = new Client(Config.BUFFER_SIZE, Config.BUFFER_SIZE);
			Utilities.log(this, "Registering client serialization IDs", false);
			registerClasses(client.getKryo());
			Utilities.log(this, "Attaching client listeners...", false);
			client.addListener(new ClientListener());
		} catch (Exception ex) {
			Utilities.log(this, "Client listener registration exception: ", false);
			ex.printStackTrace();
		}
	}
	
	private void registerClasses(Kryo kryo) {
		kryo.register(Packet.class);
		kryo.register(NetRequest.class);
		kryo.register(NetData.class);
	}
	
	private void startClient() {
		try {
			Utilities.log(this, "Starting client thread...", false);
			(new Thread(client)).start();
		} catch (Exception ex) {
			Utilities.log(this, "Client start exception: ", false);
			ex.printStackTrace();
			Utilities.log(this, "Shutting down ragnode...", false);
		}
	}
	
	private void connectClient() {
		try {
			Utilities.log(this, "Connecting to server at " + Config.SERVER_IP, false);
			client.connect(8000, InetAddress.getByName(Config.SERVER_IP), Config.BIND_PORT);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public Client getClient() {
		return client;
	}
}
