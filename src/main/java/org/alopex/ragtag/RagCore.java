package org.alopex.ragtag;

import java.util.ArrayList;

import org.alopex.ragtag.net.ServerNetworking;
import org.alopex.ragtag.net.api.APIRouter;
import org.alopex.ragtag.net.webserver.WebServer;
import org.alopex.ragtag.net.worker.WorkerManager;

public class RagCore {

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
					while(WorkerManager.getWorkers().size() == 0) {
						Thread.sleep(100);
					}
					ArrayList<Object> nums = new ArrayList<Object> ();
					for(int i = 0; i < 1000000; i++)
						nums.add(i);
					
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
