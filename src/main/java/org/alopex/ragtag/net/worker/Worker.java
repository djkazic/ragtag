package org.alopex.ragtag.net.worker;

import org.alopex.ragtag.Utilities;

import com.esotericsoftware.kryonet.Connection;

public class Worker {

	private String id;
	private Connection connection;
	
	public Worker(Connection connection) {
		connection.setIdleThreshold(0.4f);
		this.connection = connection;
	}
	
	public void handShake() {
		//TODO: implement handshake protocol with ID assignment
	}
	
	public void assignWork() {
		//TODO: assign work via this connection
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
