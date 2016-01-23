package org.alopex.ragnode;

public class NodeCore {
	
	private static ClientNetworking cn;
	
    public static void main(String[] args) {
        Utilities.log("RagNodeCore", "Initializing ragtag node...", false);
        cn = new ClientNetworking();
		cn.initialize();
    }
}
