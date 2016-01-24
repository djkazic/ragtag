package org.alopex.ragtag.net.worker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import org.alopex.ragtag.RagCore;
import org.alopex.ragtag.Utilities;
import org.alopex.ragtag.module.Job;
import org.alopex.ragtag.net.packets.NetRequest;
import org.alopex.ragtag.net.poll.SysResPoller;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
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
	
	public static void assignWork(ArrayList<String> data) {		
		ArrayList<String> container = new ArrayList<String> ();
		while((lastIndex + 1) != data.size()) {
			for(int i=0; i < workers.size(); i++) {
				Worker worker = workers.get(i);
				container = getChunk(data, worker.getShare());
				
				//Preliminary object
				Job job = new Job(container, new File("evenodd.jar"));
				
				//Check if this worker has got the job already - if so, erase the binary
				if(worker.hasJob(job.getID())) {
					job.wipeBinary();
				} else {
					worker.addJob(job);
					//TODO: switch to String ArrayList
				}
				worker.getConnection().sendTCP(new NetRequest(NetRequest.JOB, job));
				// Experimental: workers may not always update their scale before being hit with the next job
			}
			try {
				Thread.sleep(150);
			} catch(InterruptedException e) {}
		}
	}
	
	public static ArrayList<String> getChunk(ArrayList<String> data, double ratio) {
		ArrayList<String> output = new ArrayList<String> ();
		int chunkSize = 0;
		Kryo kryo = RagCore.getServerNetworking().getServer().getKryo();
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Output tempOut = new Output(baos);
			for(int i=lastIndex + 1; i < data.size() && chunkSize < (4096 * ratio); i++) {
				kryo.writeObject(tempOut, data.get(i));
				int objSize = tempOut.toBytes().length;
				output.add(data.get(i));
				chunkSize += objSize;
				tempOut.clear();
				lastIndex = i;
			}
			tempOut.close();
			System.out.println("data [" + output.get(0) + ", " + output.get(output.size() - 1) + " ] queued.");
			Utilities.log("WorkerManager", "Ending on number " + (lastIndex + 1) + " of " + data.size(), false);
			if(output.size() > 0) {
				return output;
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
