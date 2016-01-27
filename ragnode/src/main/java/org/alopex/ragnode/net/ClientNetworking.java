package org.alopex.ragnode.net;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

import org.alopex.ragnode.core.Config;
import org.alopex.ragnode.core.Utilities;
import org.alopex.ragnode.module.Job;
import org.alopex.ragnode.net.packets.NetData;
import org.alopex.ragnode.net.packets.NetRequest;
import org.alopex.ragnode.net.packets.Packet;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;

public class ClientNetworking {

	private Client client;
	
	public boolean initialize() {
		return registerClientListeners() && startClient() && connectClient();
	}
	
	private boolean registerClientListeners() {
		try {
			client = new Client(Config.BUFFER_SIZE, Config.BUFFER_SIZE);
			Utilities.log(this, "Registering client serialization IDs", false);
			registerClasses(client.getKryo());
			Utilities.log(this, "Attaching client listeners...", false);
			client.addListener(new ClientListener());
			client.addListener(new ChunkListener());
			return true;
		} catch (Exception ex) {
			Utilities.log(this, "Client listener registration exception: ", false);
			ex.printStackTrace();
		}
		return false;
	}
	
	private void registerClasses(Kryo kryo) {
		kryo.register(Packet.class);
		kryo.register(NetRequest.class);
		kryo.register(NetData.class);
		kryo.register(Job.class);
		kryo.register(ArrayList.class);
		kryo.register(HashMap.class);
		kryo.register(byte[].class);
		kryo.register(Class.class);
		kryo.register(Class[].class);
	}
	
	private boolean startClient() {
		try {
			Utilities.log(this, "Starting client thread...", false);
			(new Thread(client)).start();
			return true;
		} catch (Exception ex) {
			Utilities.log(this, "Client start exception: ", false);
			ex.printStackTrace();
			Utilities.log(this, "Shutting down ragnode...", false);
			return false;
		}
	}
	
	private boolean connectClient() {
		try {
			InetAddress potentialIA = client.discoverHost(Config.DISC_PORT, 4000);
			if(potentialIA != null) {
				Utilities.log(this, "Connecting to discovered server at " + potentialIA, false);
				client.connect(8000, potentialIA, Config.BIND_PORT, Config.DISC_PORT);
			} else {
				Utilities.log(this, "Falling back to hard-coded node at " + Config.SERVER_IP, false);
				Utilities.log(this, "Connecting to server at " + Config.SERVER_IP, false);
				client.connect(8000, InetAddress.getByName(Config.SERVER_IP), Config.BIND_PORT, Config.DISC_PORT);		
			}
			return true;
		} catch (Exception ex) {
			Utilities.log(this, "", false);
			Utilities.log(this, "Failure to connect to server", false);
		}
		return false;
	}
	
	public Client getClient() {
		return client;
	}
}
