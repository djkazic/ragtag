package org.alopex.ragtag.net.worker;

import java.util.ArrayList;

import org.alopex.ragtag.core.Utilities;
import org.alopex.ragtag.module.Job;
import org.alopex.ragtag.net.packets.NetRequest;

import com.esotericsoftware.kryonet.Connection;

public class Worker implements Comparable<Worker> {

	private String id;
	private Connection connection;
	private int benchmark;
	private double share = 1;
	private double load;
	private ArrayList<Job> jobs = new ArrayList<Job> ();
	
	public Worker(Connection connection) {
		Utilities.log(this, "New inbound worker", false);
		connection.setIdleThreshold(0.4f);
		this.connection = connection;
	}
	
	public void handShake() {
		connection.sendTCP(new NetRequest(NetRequest.HANDSHAKE, null));
	}
	
	public String getID() {
		return id;
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public void disconnect() {
		connection.close();
	}
	
	public void setID(String handshakeID) {
		id = handshakeID;
	}
	
	public int getBenchmark() {
		return benchmark;
	}
	
	public void setBenchmark(int iterations) {
		benchmark = iterations;
	}
	
	public double getShare() {
		return share;
	}
	
	public void setShare(double inShare) {
		share = inShare;
	}

	public void setLoad(double sysLoad) {
		Utilities.log(this, "Worker " + id.substring(0, 5) + " load detected: " + load, false);
		load = sysLoad;
	}

	public void pollSysReq() {
		connection.sendTCP(new NetRequest(NetRequest.SYSRES, null));
	}

	@Override
	public boolean equals(Object other) {
		if(other instanceof Worker) {
			Worker tempWorker = (Worker) other;
			if(id != null) {
				return id.equals(tempWorker.getID());
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return connection.hashCode() + id.hashCode();
	}

	public int compareTo(Worker worker) {
		if(share == 0 || worker.getShare() == 0) {
			return (int) (worker.load - load);
		} else {
			return (int) (share - worker.getShare());
		}
	}
	
	public void addJob(Job job) {
		jobs.add(job);
	}
	
	public boolean hasJob(String jobName) {
		if(jobs.size() > 0) {
			for(int i = 0; i < jobs.size(); i++) {
				Job job = jobs.get(i);
				if(job != null && job.getID() != null) {
					return (job.getID().equals(jobName));
				}
			}
		}
		return false;
	}
}
