package org.alopex.ragtag.core;

import java.io.File;
import java.util.ArrayList;

import org.alopex.ragtag.module.Job;
import org.alopex.ragtag.net.ServerNetworking;
import org.alopex.ragtag.net.api.APIRouter;
import org.alopex.ragtag.net.webserver.WebServer;
import org.alopex.ragtag.net.worker.WorkerManager;

public class RagCore {

	public static ArrayList<Job> jobList = new ArrayList<Job> ();
	
	public static Thread webServerThread;
	public static WorkerManager wm;
	private static ServerNetworking sn;
	
	public static void main(String[] args) {
		Utilities.log("RagCore", "Initializing ragtag server...", false);
		
		webServerThread = new Thread(new WebServer());
		webServerThread.start();
		APIRouter.init();
		wm = new WorkerManager();
		sn = new ServerNetworking();
		sn.initialize();
		
		//DEMO - remove for production
		(new Thread(new Runnable() {
			public void run() {
				try {
					while(WorkerManager.getWorkers().size() < 1) {
						Thread.sleep(100);
					}
					
					ArrayList<String> nums = new ArrayList<String> ();
					for(int i = 0; i < 100000; i++)
						nums.add(i + "");
					
					WorkerManager.assignWork(new File("evenodd.jar"), nums);
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		})).start();
	}
	
	public static ServerNetworking getServerNetworking() {
		return sn;
	}
	
	public static boolean attemptExec(String fileName, final ArrayList<String> dataSet) {
		Utilities.log("REST-Exec()", "Execution for " + fileName + " detected", false);
		try {
			final File testForExistence = new File(fileName);
			if(testForExistence.exists() && testForExistence.length() > 0 && testForExistence.canExecute()) {
				(new Thread(new Runnable() {
					public void run() {
						WorkerManager.assignWork(testForExistence, dataSet);
					}
				})).start();
				return true;
			} else {
				return false;
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
}
