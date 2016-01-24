package org.alopex.ragtag.net;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import org.alopex.ragtag.Config;
import org.alopex.ragtag.Utilities;
import org.alopex.ragtag.module.Job;
import org.alopex.ragtag.module.Process;
import org.alopex.ragtag.net.packets.NetData;
import org.alopex.ragtag.net.packets.NetRequest;
import org.alopex.ragtag.net.packets.Packet;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;

public class ServerNetworking {

	private Server server;
	
	public void initialize() {
		registerServerListeners();
		startServer();
	}
	
	private void registerServerListeners() {
		try {
			server = new Server(Config.BUFFER_SIZE, Config.BUFFER_SIZE);
			registerClasses(server.getKryo());
			server.addListener(new ServerListener());
		} catch (Exception ex) {
			Utilities.log(this, "Server listener registration exception: ", false);
			ex.printStackTrace();
		}
	}
	
	private void registerClasses(Kryo kryo) {
		kryo.register(Packet.class);
		kryo.register(NetRequest.class);
		kryo.register(NetData.class);
		kryo.register(Job.class);
		kryo.register(ArrayList.class);
		kryo.register(HashMap.class);
		kryo.register(Method.class);
		kryo.register(byte[].class);
		kryo.register(Class.class);
		kryo.register(Process.class);
		kryo.register(Class[].class);
	}
	
	private void startServer() {
		try {
			Utilities.log(this, "Binding server port...", false);
			server.bind(Config.BIND_PORT);
			Utilities.log(this, "Starting server thread...", false);
			server.start();
		} catch (Exception ex) {
			Utilities.log(this, "Server start/bind exception: ", false);
			ex.printStackTrace();
			Utilities.log(this, "Shutting down ragtag...", false);
			System.exit(0);
		}
	}
	
	public Server getServer() {
		return server;
	}
}
