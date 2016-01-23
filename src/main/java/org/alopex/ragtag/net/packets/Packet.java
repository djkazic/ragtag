package org.alopex.ragtag.net.packets;

public class Packet {

	private byte type;
	private Object payload;
	
	public byte getType() {
		return type;
	}
	
	public Object getPayload() {
		return payload;
	}
}
