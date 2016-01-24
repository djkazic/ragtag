package org.alopex.ragtag.net.webserver;

import java.net.*;
import java.io.*;
import java.util.*;

import org.alopex.ragtag.Utilities;

public class WebServer implements Runnable {

	private static int port = 8080;
	private static String htmlHome = "web";

	public void run() {
		//Open listener socket
		ServerSocket socket = null;
		try {
			socket = new ServerSocket(port);
		} catch (Exception e) {
			Utilities.log(this, "Could not start server: " + e, false);
			System.exit(-1);
		}
		Utilities.log(this, "Internal webserver accepting connections on port " + port, false);

		//Request handling
		Socket requestConnection = null;
		while(true) {
			try {
				requestConnection = socket.accept();
				BufferedReader in = new BufferedReader(new InputStreamReader(requestConnection.getInputStream()));
				OutputStream out = new BufferedOutputStream(requestConnection.getOutputStream());
				PrintStream pout = new PrintStream(out);
				String request = in .readLine();
				if(request == null)
					continue;
				while (true) {
					String misc = in .readLine();
					if(misc == null || misc.length() == 0)
						break;
				}

				if(!request.startsWith("GET") || request.length() < 14 ||
						!(request.endsWith("HTTP/1.0") || request.endsWith("HTTP/1.1"))) {
					errorReport(pout, requestConnection, "400", "Bad Request",
								"Invalid request.");
				} else {
					String req = request.substring(4, request.length() - 9).trim();
					if(req.indexOf("..") != -1 || req.indexOf("/.ht") != -1 || req.endsWith("~")) {
						errorReport(pout, requestConnection, "403", "Forbidden",
									"Access denied: could not serve requested URL.");
					} else {
						String path = htmlHome + "/" + req;
						File f = new File(path);
						if(f.isDirectory() && !path.endsWith("/")) {
							pout.print("HTTP/1.0 301 Moved Permanently\r\n" +
									   "Location: http://" +
									   requestConnection.getLocalAddress().getHostAddress() + ":" +
									   requestConnection.getLocalPort() + "/" + req + "/\r\n\r\n");
						} else {
							if(f.isDirectory()) {
								path = path + "index.html";
								f = new File(path);
							}
							
							try {
								InputStream file = new FileInputStream(f);
								pout.print("HTTP/1.0 200 OK\r\n" +
										   "Content-Type: " + guessContentType(path) + "\r\n" +
										   "Date: " + new Date() + "\r\n" +
										   "Server: FileServer 1.0\r\n\r\n");
								sendFile(file, out);
							} catch (FileNotFoundException e) {
								errorReport(pout, requestConnection, "404", "Not Found",
											"The requested URL was not found on this server.");
							}
						}
					}
				}
				out.flush();
			} catch (IOException e) {
				System.err.println(e);
			}
			
			try {
				if(requestConnection != null) {
					requestConnection.close();
				} else {
					socket.close();
				}
			} catch (IOException e) {
				System.err.println(e);
			}
		}
	}

	private static void errorReport(PrintStream pout, Socket connection,
									String code, String title, String msg) {
		pout.print("HTTP/1.0 " + code + " " + title + "\r\n" +
				   "\r\n" +
				   "<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\r\n" +
				   "<TITLE>" + code + " " + title + "</TITLE>\r\n" +
				   "</HEAD><BODY>\r\n" +
				   "<H1>" + title + "</H1>\r\n" + msg + "<P>\r\n" +
				   "<HR><ADDRESS>FileServer 1.0 at " +
				   connection.getLocalAddress().getHostName() +
				   " Port " + connection.getLocalPort() + "</ADDRESS>\r\n" +
				   "</BODY></HTML>\r\n");
	}

	private static String guessContentType(String path) {
		if(path.endsWith(".html") || path.endsWith(".htm"))
			return "text/html";
		else if(path.endsWith(".txt") || path.endsWith(".java"))
			return "text/plain";
		else if(path.endsWith(".gif"))
			return "image/gif";
		else if(path.endsWith(".class"))
			return "application/octet-stream";
		else if(path.endsWith(".jpg") || path.endsWith(".jpeg"))
			return "image/jpeg";
		else
			return "text/plain";
	}

	private static void sendFile(InputStream file, OutputStream out) {
		try {
			byte[] buffer = new byte[1000];
			while (file.available() > 0)
				out.write(buffer, 0, file.read(buffer));
		} catch (IOException e) {
			System.err.println(e);
		}
	}
}