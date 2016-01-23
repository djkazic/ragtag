package org.alopex.ragtag.modulereflect;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class Module {
	
	private Object[] data;
	private Method process = null;
	
	public final long benchmark()
	{
		if(data == null)
			return -1;
		long start = System.nanoTime();
		try {
			process.invoke(data[0]);
		} catch (Exception e) {
			return -1;
		}
		long end = System.nanoTime();
		return end-start;
	}
	
	public final ArrayList<Object> execute()
	{
		ArrayList<Object> outData = new ArrayList<Object>();
		Class<?> paramType = this.process.getParameterTypes()[0].getClass();
		
		for(Object datum : this.data)
		{
			try {
				outData.add(this.process.invoke(paramType.cast(datum)));
			} catch (Exception e)
			{
				try{
					outData.add(this.process.invoke( (paramType.getField("TYPE").getType().cast(datum))));
				} catch(Exception r)
				{
					e.printStackTrace();
				}
			}
		}
		return outData;
	}

	public boolean receive(Object[] data) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
	{
		Class<?> returnType = this.process.getReturnType();
		try {
			System.out.println(returnType + " " + data[0].getClass().getField("TYPE").get(null));
		} catch (Exception e) { };
		if(!(returnType.isInstance(data[0]) || returnType.equals(data[0].getClass().getField("TYPE").get(null))))
				return false;
		this.data = data;
		return true;
	}
	
	public boolean setProcess(Method process)
	{
		this.process = process;
		return true;
	}
}