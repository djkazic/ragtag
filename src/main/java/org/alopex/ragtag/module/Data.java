package org.alopex.ragtag.module;

public class Data<T> {
	T payload;
	
	public Data(T payload)
	{
		this.payload = payload;
	}
	
	public Object process()
	{
		return payload.hashCode();
	}

}
