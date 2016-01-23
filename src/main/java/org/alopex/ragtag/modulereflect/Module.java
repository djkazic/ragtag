package org.alopex.ragtag.modulereflect;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.alopex.ragtag.Utilities;
import org.apache.commons.lang3.ClassUtils;

public class Module {

	private Object[] data;
	private Method process = null;

	/* Runs the job given to the module a given number of times
	 * 
	 * @param timesToRun the number of times to run the test job 
	 * @return time taken in nanoseconds. Returns -1 if benchmark could not be run
	 */
	public final long benchmark(int throwawayIterations) {
		//Check to make sure the benchmark can actually run
		if(data == null)
			throw new NullPointerException("Data is null");
		if(process == null)
			throw new NullProcessException("Process not found in benchmark method of Module");
		
		//Optimize the loop via JVM native compilation
		for(int i = 0; i < throwawayIterations; i++) {
			try {
				process.invoke(null, data[0]);
			} catch (Exception e) {
				return -1;
			}
		}
		
		long start = System.nanoTime();
		try {
			process.invoke(null, data[0]);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		long end = System.nanoTime();
		
		//Return the time taken in nanoseconds
		return end-start;
	}

	public final ArrayList<Object> execute() {
		ArrayList<Object> outData = new ArrayList<Object>();
		
		//Currently only works if there is one parameter...
		Class<?> paramType = this.process.getParameterTypes()[0];
		
		//Working with primitives suck. Probably going to deprecate
		if(ClassUtils.isPrimitiveOrWrapper(paramType)) {
			if(paramType.isPrimitive()) {
				paramType = ClassUtils.primitiveToWrapper(paramType);
			}
		}
		
		//Calls process on each piece of data
		for(Object datum : this.data) {
			try {
				outData.add(this.process.invoke(
							null,
						    paramType.cast(datum)
						   ));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return outData;
	}

	//Receives data and then does data validation. Must have a process already defined
	public boolean receiveData(Object[] data) {
		Class<?> inType = this.process.getParameterTypes()[0];
		if(!(inType.isInstance(data[0]))) {
			//If the process method has an input type different from the input data
			Utilities.log(this, "Type mismatch detected: inType = " + inType.getName() 
							  + " vs data type = " + data.getClass(), false);
			return false;
		}
		this.data = data;
		return true;
	}

	//Just defines the process
	public boolean setProcess(Method process) {
		this.process = process;
		return true;
	}
}