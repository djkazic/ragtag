package org.alopex.ragtag;

import java.util.ArrayList;

import org.alopex.ragtag.module.Job;
import org.alopex.ragtag.net.ServerNetworking;
import org.alopex.ragtag.net.api.APIRouter;
import org.alopex.ragtag.net.webserver.WebServer;
import org.alopex.ragtag.net.worker.WorkerManager;

public class RagCore {

	public static ArrayList<Job> jobList = new ArrayList<Job> ();;
	
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
		
		//DEBUG
		(new Thread(new Runnable() {
			public void run() {
				try {
					while(WorkerManager.getWorkers().size() < 2) {
						Thread.sleep(100);
					}
					
					ArrayList<String> nums = new ArrayList<String> ();
					for(int i = 0; i < 40000; i++)
						nums.add(i + "");
					
					WorkerManager.assignWork(nums);
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		})).start();
	}
	
	public static ServerNetworking getServerNetworking() {
		return sn;
	}
}
