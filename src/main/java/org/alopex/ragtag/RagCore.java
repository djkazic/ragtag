package org.alopex.ragtag;

import org.alopex.ragtag.net.ServerNetworking;
import org.alopex.ragtag.net.worker.WorkerManager;

public class RagCore {

	public static WorkerManager wm;
	private static ServerNetworking sn;
	
	public static void main(String[] args) {
		Utilities.log("RagCore", "Initializing ragtag server...", false);
		wm = new WorkerManager();
		sn = new ServerNetworking();
		sn.initialize();
	}
}
