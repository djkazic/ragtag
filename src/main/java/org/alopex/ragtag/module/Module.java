package org.alopex.ragtag.module;

public abstract class Module {
	private static final int BENCHMARK_LENGTH = 6000; //In milliseconds
	private boolean done = false;
	public final long benchmark()
	{
		long start = System.nanoTime();
		long end = System.nanoTime();
		Long numOperations = (long) 0;
		do{
			
		}while((end - start) < ((long) (Math.pow(10, 5) * Module.BENCHMARK_LENGTH)) && !done);
	}
	
	public final void execute()
	{
		done = false;
		this.job();
		done = true;
	}
	
	protected abstract void job();
	protected abstract void define();
}
