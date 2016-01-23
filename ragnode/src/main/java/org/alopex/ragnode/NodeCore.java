package org.alopex.ragnode;

import java.net.NetworkInterface;
import java.util.Enumeration;

import org.alopex.ragnode.net.ClientNetworking;

public class NodeCore {
	
	private static ClientNetworking cn;
	private static String id;
	
    public static void main(String[] args) {
        Utilities.log("RagNodeCore", "Initializing ragtag node...", false);
        generateId();
        cn = new ClientNetworking();
		cn.initialize();	
    }
    
    public static ClientNetworking getClientNetworking() {
    	return cn;
    }
    
    public static String getId() {
    	return id;
    }
    
    private static void generateId() {
    	try {
    		String firstInterfaceFound = null;        
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			StringBuilder sb = new StringBuilder();
			while(networkInterfaces.hasMoreElements()){
				NetworkInterface network = networkInterfaces.nextElement();
				byte[] bmac = network.getHardwareAddress();
				if(bmac != null && bmac[0] != 0) {
					for(int i=0; i < bmac.length; i++) {
						sb.append(String.format("%02X%s", bmac[i], (i < bmac.length - 1) ? "-" : ""));        
					}
				}
			}
			if(!sb.toString().isEmpty() && firstInterfaceFound == null) {
				id = Utilities.base64(sb.toString());
			} else {
				Utilities.log("NodeCore", "Interfaces are null, falling back to config mutex", false);
				id = Utilities.base64(Utilities.randomMACAddress());
			}			
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
    }
}
