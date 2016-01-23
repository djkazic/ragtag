package org.alopex.ragtag.net.worker;

import org.alopex.ragtag.Utilities;
import org.alopex.ragtag.net.packets.NetRequest;

import com.esotericsoftware.kryonet.Connection;

@SuppressWarnings("unused")
public class Worker {

	private String id;
	private Connection connection;
	private int benchmark;
	private double performance;
	
	public Worker(Connection connection) {
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
	
	public void setPerformance(double d) {
		performance = d;
	}

	@Override
	public boolean equals(Object other) {
		if(other instanceof Worker) {
			Worker tempWorker = (Worker) other;
			return id.equals(tempWorker.getID());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return connection.hashCode() + id.hashCode();
	}
}
