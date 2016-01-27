package org.alopex.ragtag.net.webserver;

import java.net.ServerSocket;
import java.net.Socket;

import org.alopex.ragtag.Utilities;

public final class WebServer implements Runnable {

	public void run() {
		int port = 8080;
		Utilities.log(this, "Initializing internal server on port " + port, false);
		try {
			ServerSocket webSocket = new ServerSocket(port);
			try {
				while(true) {
					Socket connectionSocket = webSocket.accept();
					HttpRequest request = new HttpRequest(connectionSocket);

					Thread thread = new Thread(request);
					thread.start();
				}
			} catch(Exception ex) {
				ex.printStackTrace();
				webSocket.close();
			}
			webSocket.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}