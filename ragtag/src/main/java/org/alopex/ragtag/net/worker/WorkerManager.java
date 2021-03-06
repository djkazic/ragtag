package org.alopex.ragtag.net.worker;

import java.io.File;
import java.util.ArrayList;

import org.alopex.ragtag.core.Utilities;
import org.alopex.ragtag.module.Job;
import org.alopex.ragtag.net.packets.NetRequest;
import org.alopex.ragtag.net.poll.SysResPoller;

import com.esotericsoftware.kryonet.Connection;

public class WorkerManager {
	
	private static ArrayList<Worker> workers;
	//private static HashMap<Worker, int[]> lastProcessed;
	private static int lastIndex = -1;
	
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
	
	public static void calculateShares() {
		if(workers.size() > 0) {
			int total = 0;
			for(Worker worker : workers) {
				total += worker.getBenchmark();
			}
			for(Worker worker : workers) {
				worker.setShare((double) worker.getBenchmark() / total);
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
	
	public static void assignWork(File file, ArrayList<String> data) {		
		ArrayList<String> container;
		Utilities.log("WorkerManager", "Starting loop for work assign: " + file, false);
		outerLoop:
		while((lastIndex + 1) != data.size()) {
			for(int i=0; i < workers.size(); i++) {
				Worker worker = workers.get(i);
				container = getChunk(data, worker.getShare());

				if(container != null) {
					//Preliminary object with file-read bytes
					Job job = new Job(container, file);

					if(job.isValid()) {
						//Check if this worker has got the job already - if so, erase the binary bytes
						if(worker.hasJob(job.getID())) {
							job.wipeBinary();
						} else {
							//Queue the job in worker storage
							worker.addJob(job);
						}
						worker.getConnection().sendTCP(new NetRequest(NetRequest.JOB, job));
					} else {
						Utilities.log("WorkerManager", "Job canceled: invalid", false);
						break outerLoop;
					}
					// Experimental: workers may not always update their scale before being hit with the next job
				} else {
					Utilities.log("WorkerManager", "Job execution halted: finished (" + lastIndex + " | " + data.size() + ")", false);
					break outerLoop;
				}
			}
			try {
				Thread.sleep(150);
			} catch(InterruptedException e) {}
		}
		Utilities.log("WorkerManager", "Job execution halted: assignment loop terminated", false);
	}
	
	public static ArrayList<String> getChunk(ArrayList<String> data, double ratio) {
		ArrayList<String> output = new ArrayList<String> ();
		int chunkSize = 0;
		try {
			for(int i=lastIndex + 1; i < data.size(); i++) {
				int objSize = data.get(i).length();
				chunkSize += objSize;
				
				double target = (3840 * ratio);
				output.add(data.get(i));
				lastIndex = i;

				if(chunkSize > target) {
					Utilities.log("WorkerManager", "Exceeded chunksize of: " + target + " | " + chunkSize, false);
					break;
				}
			}
			Utilities.log("WorkerManager", "Ending on number " + (lastIndex + 1) + " of " + data.size(), false);
			if(output.size() > 0) {
				return output;
			} else {
				Utilities.log("WorkerManager", "Empty output: " + output, false);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public static ArrayList<Worker> getWorkers() {
		return workers;
	}
}
