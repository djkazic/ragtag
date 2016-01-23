package org.alopex.ragnode;

import java.net.InetAddress;

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
			registerClasses(client.getKryo());
			client.addListener(new ClientListener());
		} catch (Exception ex) {
			Utilities.log(this, "Client listener registration exception: ", false);
			ex.printStackTrace();
		}
	}
	
	private void registerClasses(Kryo kryo) {
		//TODO: Register classes with kryo here
	}
	
	private void startClient() {
		try {
			Utilities.log(this, "Starting client thread...", false);
			client.start();
		} catch (Exception ex) {
			Utilities.log(this, "Client start exception: ", false);
			ex.printStackTrace();
			Utilities.log(this, "Shutting down ragnode...", false);
		}
	}
	
	private void connectClient() {
		try {
			client.connect(8000, InetAddress.getByName(Config.SERVER_IP), Config.BIND_PORT);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
