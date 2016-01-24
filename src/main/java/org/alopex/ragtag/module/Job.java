package org.alopex.ragtag.module;

import java.util.ArrayList;
import java.util.HashMap;

import org.alopex.ragtag.Utilities;

public class Job {

	private ArrayList<Object> data;
	private Class<?> processClass;
	
	public Job() {}
	
	public Job(ArrayList<Object> data, Class<?> processClass) {
		if(data.size() > 0 && processClass != null) {
			Class<?> inType = processClass.getMethods()[0].getParameterTypes()[0];
			if(!(inType.isInstance(data.get(0)))) {
				//If the process method has an input type different from the input data
				Utilities.log(this, "Type mismatch detected: inType = " + inType.getName() 
								  + " vs data type = " + data.getClass(), false);
				//TODO: Throw
				return;
			}
			this.data = data;
			this.processClass = processClass;
		} else {
			Utilities.log(this, "Failure to create job: bad data or bad process method specified", false);
			//TODO: Throw
			return;
		}
	}

	/* Runs the job given to the module a given number of throwaway cycles
	 * 
	 * @param timesToRun the number of times to run the test job 
	 * @return time taken in nanoseconds. Returns -1 if benchmark could not be run
	 */
	public final long benchmark(int throwawayIterations) {
		//Check to make sure the benchmark can actually run
		if(data == null)
			throw new NullPointerException("Data is null");
		if(processClass == null)
			throw new NullPointerException("Process class not defined");
		
		//Optimize the loop via JVM native compilation
		for(int i = 0; i < throwawayIterations; i++) {
			try {
				processClass.getMethods()[0].invoke(null, data.get(0));
			} catch (Exception e) {
				return -1;
			}
		}
		
		long start = System.nanoTime();
		try {
			processClass.getMethods()[0].invoke(null, data.get(0));
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		long end = System.nanoTime();
		
		//Return the time taken in nanoseconds
		return end-start;
	}

	public final Object[] execute() {
		HashMap<Object, Object> outData = new HashMap<Object, Object>();
		
		//Currently only works if there is one parameter...
		Class<?> paramType = this.processClass
				.getMethods()[0]
						.getParameterTypes()[0];
		
		long start = System.nanoTime();
		//Calls process on each piece of data
		for(Object datum : this.data) {
			try {
				outData.put(datum, this.processClass.getMethods()[0].invoke(null, paramType.cast(datum)));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		long end = System.nanoTime(); 
		return new Object[] {end - start, outData};
	}
	
	public ArrayList<Object> dataSet() {
		return data;
	}
}