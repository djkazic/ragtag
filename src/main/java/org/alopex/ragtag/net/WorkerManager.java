package org.alopex.ragtag.net;

import java.util.ArrayList;

import org.alopex.ragtag.Utilities;

import com.esotericsoftware.kryonet.Connection;

public class WorkerManager {
	
	private static ArrayList<Worker> workers;
	
	public WorkerManager() {
		workers = new ArrayList<Worker> ();
	}

	public static void registerWorker(Worker worker) {
		if(!workers.contains(worker)) {
			workers.add(worker);
			worker.handShake();
		} else {
			Utilities.log("WorkerManager", "Collision detected: duplicate worker " + worker.getID(), false);
		}
	}
	
	public static void disconnectWorker(Connection connection) {
		if(workers.size() > 0) {
			for(Worker worker : workers) {
				if(worker.getConnection().equals(connection)) {
					workers.remove(worker);
					Utilities.log("WorkerManager", "Disconnecting worker " + worker.getID(), false);
					worker.disconnect();
				}
			}
		}
	}
}
