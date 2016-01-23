package org.alopex.ragtag.net.worker;

import java.util.ArrayList;

import org.alopex.ragtag.Utilities;

import com.esotericsoftware.kryonet.Connection;

public class WorkerManager {
	
	private static ArrayList<Worker> workers;
	
	public WorkerManager() {
		Utilities.log("WorkerManager", "Starting WorkerManager instance...", false);
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
		Worker worker;
		if((worker = getWorker(connection)) != null) {
			workers.remove(worker);
			Utilities.log("WorkerManager", "Disconnecting worker " + worker.getID(), false);
			worker.disconnect();
		}
	}
	
	public static Worker getWorker(Connection connection) {
		if(workers.size() > 0) {
			for(Worker worker : workers) {
				if(worker.getConnection().equals(connection)) {
					return worker;
				}
			}
		}
		return null;
	}
	
	public static void calculatePerformance() {
		if(workers.size() > 0) {
			Worker max = workers.get(0);
			for(Worker worker : workers) {			
				if(worker.getBenchmark() > max.getBenchmark()) {
					max = worker;
				}
			}
			for(Worker worker : workers) {
				worker.setPerformance((double) worker.getBenchmark() / max.getBenchmark());
			}
		}
	}
	
	public static void assignWork() {
		calculatePerformance();
		//Get module details
		//Assign workers work based on their performance score (percentages of work)
		//Break work into workers.size() chunks, assign X number of chunks at a time (load)
		//Those with higher performance get multiple loads, whereas lower ones get one
		//Load load = breakWork(module, worker)
		//worker.assignLoad(load)
	}
}
