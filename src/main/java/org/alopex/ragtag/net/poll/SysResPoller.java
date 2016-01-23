package org.alopex.ragtag.net.poll;

import org.alopex.ragtag.net.worker.WorkerManager;

public class SysResPoller implements Runnable {

	public void run() {
		while(true) {
			try {
				WorkerManager.pollWorkerSysReq();
				Thread.sleep(2500);
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
