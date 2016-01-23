package org.alopex.ragnode.net.packets;

public class Packet {

	private byte type;
	private Object payload;
	
	public Packet() { }
	
	public Packet(byte type, Object payload) {
		this.type = type;
		this.payload = payload;
	}
	
	public byte getType() {
		return type;
	}
	
	public Object getPayload() {
		return payload;
	}
}
