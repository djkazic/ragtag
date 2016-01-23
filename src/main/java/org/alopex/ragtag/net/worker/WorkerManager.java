package org.alopex.ragtag.net.worker;

import java.util.ArrayList;

import org.alopex.ragtag.Utilities;
import org.alopex.ragtag.net.poll.SysResPoller;

import com.esotericsoftware.kryonet.Connection;

public class WorkerManager {
	
	private static ArrayList<Worker> workers;
	
	public WorkerManager() {
		Utilities.log("WorkerManager", "Starting WorkerManager instance...", false);
		workers = new ArrayList<Worker> ();
		
		Utilities.log("WorkerManager", "Starting SysReqPoller instance...", false);
		Thread sysResThread = new Thread(new SysResPoller());
		sysResThread.setName("System Res Poller");
		sysResThread.start();
	}

	public static void registerWorker(Worker worker) {
		if(!workers.contains(worker)) {
			workers.add(worker);
			Utilities.log("WorkerManager", "Executing worker handshake", false);
			worker.handShake();
		} else {
			Utilities.log("WorkerManager", "Collision detected: duplicate worker " + worker.getID(), false);
		}
	}
	
	public static void disconnectWorker(Connection connection) {
		Worker worker;
		if((worker = getWorker(connection)) != null) {
			workers.remove(worker);
			Utilities.log("WorkerManager", "Worker " + worker.getID() + " disconnecting...", false);
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
	
	public static void pollWorkerSysReq() {
		if(workers.size() > 0) {
			for(int i=0; i < workers.size(); i++) {
				workers.get(i).pollSysReq();
			}
		}
	}
	
	public static void assignWork() {
		calculatePerformance();
	}
}
