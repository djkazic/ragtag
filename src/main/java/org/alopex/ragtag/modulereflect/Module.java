package org.alopex.ragtag.modulereflect;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.apache.commons.lang3.ClassUtils;

public class Module {
	
	private Object[] data;
	private Process process = null;
	private Method method = null;
	
	public final long benchmark()
	{
		if(data == null)
			return -1;
		long start = System.nanoTime();
		try {
			method.invoke(data[0]);
		} catch (Exception e) {
			return -1;
		}
		long end = System.nanoTime();
		return end-start;
	}
	
	public final ArrayList<Object> execute()
	{
		ArrayList<Object> outData = new ArrayList<Object>();
		Class<?> paramType = this.method.getParameterTypes()[0];
//		if(ClassUtils.isPrimitiveOrWrapper(paramType))
//		{
//			if(paramType.isPrimitive())
//			{
//				paramType = ClassUtils.primitiveToWrapper(paramType);
//			}
//		}
		Object datums = paramType.cast(this.data[0]);
		System.out.println(datums.getClass());
		for(Object datum : this.data)
		{
			try {
				outData.add(
						this.method.invoke(new Process(),
								paramType.cast(datum)));
			} catch (Exception e)
			{
					e.printStackTrace();
			}
		}
		return outData;
	}

	public boolean receive(Object[] data) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
	{
		Class<?> inType = this.method.getParameterTypes()[0];
		if(!(inType.isInstance(data[0]) || inType.equals(data[0].getClass().getField("TYPE").get(null))))
				return false;
		this.data = data;
		return true;
	}
	
	public boolean setProcess(Process process)
	{
		this.process = process;
		this.method = process.getClass().getMethods()[0];
		return true;
	}
}