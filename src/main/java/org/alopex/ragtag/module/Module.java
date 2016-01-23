package org.alopex.ragtag.module;

import java.util.ArrayList;

public abstract class Module<D> {
	
	public Data<D>[] data;
	
	public final long benchmark(Data<D> datum)
	{
		long start = System.nanoTime();
		datum.process();
		long end = System.nanoTime();
		return end-start;
	}
	
	public final void execute()
	{
		ArrayList<Object> outData = new ArrayList<Object>();
		for(Data<D> datum: data)
		{
			outData.add(datum.process());
		}
	}

	protected abstract void define();
}
