package org.alopex.ragnode;

import org.alopex.ragnode.net.ClientNetworking;

public class NodeCore {
	
	private static ClientNetworking cn;
	private static String id;
	
    public static void main(String[] args) {
        Utilities.log("RagNodeCore", "Initializing ragtag node...", false);
        cn = new ClientNetworking();
		cn.initialize();
		generateId();
    }
    
    public static ClientNetworking getClientNetworking() {
    	return cn;
    }
    
    public static String getId() {
    	return id;
    }
    
    private static void generateId() {
    	//TODO: set ID here
    }
}
