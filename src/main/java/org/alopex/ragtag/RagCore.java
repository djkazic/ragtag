package org.alopex.ragtag;

import org.alopex.ragtag.net.ServerNetworking;

public class RagCore {
	
	private static ServerNetworking sn;
	
	public static void main(String[] args) {
		Utilities.log("RagCore", "Initializing ragtag server...", false);
		sn = new ServerNetworking();
		sn.initialize();
	}
}
