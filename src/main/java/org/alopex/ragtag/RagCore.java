package org.alopex.ragtag;

import org.alopex.ragtag.net.Networking;

public class RagCore {
	
	private static Networking networking;
	
	public static void main(String[] args) {
		Utilities.log("RagCore", "Initializing ragtag server...", false);
		networking = new Networking();
		networking.initialize();
	}
}
