package org.alopex.ragtag.net.worker;

import org.alopex.ragtag.Utilities;
import org.alopex.ragtag.net.packets.NetRequest;

import com.esotericsoftware.kryonet.Connection;

@SuppressWarnings("unused")
public class Worker {

	private String id;
	private Connection connection;
	private int benchmark;
	private double share;
	private double load;
	
	public Worker(Connection connection) {
		Utilities.log(this, "New inbound worker", false);
		connection.setIdleThreshold(0.4f);
		this.connection = connection;
	}
	
	public void handShake() {
		connection.sendTCP(new NetRequest(NetRequest.HANDSHAKE, null));
	}
	
	public void assignLoad() {
		//TODO: method variable load
		//Assigns a load to this worker
	}
	
	public String getID() {
		return id;
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public void disconnect() {
		int connNumber = connection.getID();
		connection.close();
		if(id != null) {
			Utilities.log(this, "Worker " + id + " disconnected", false);
		} else {
			Utilities.log(this, "Worker disconnected (id null, conn #" + connNumber + ")", false);
		}
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
}
