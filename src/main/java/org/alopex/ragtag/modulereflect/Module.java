package org.alopex.ragtag.modulereflect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import org.apache.commons.lang3.ClassUtils;

public class Module {

	private Object[] data;
	private Method process = null;

	/* Runs the job given to the module a given number of times
	 * 
	 * @param timesToRun the number of times to run the test job 
	 * @return time taken in nanoseconds. Returns -1 if benchmark could not be run
	 */
	public final long benchmark(int timesToRun)
	{
		//Check to make sure the benchmark can actually run
		if(data == null)
			throw new NullPointerException("Data is null");
		if(process == null)
			throw new NullProcessException("Process not found in benchmark method of Module");
		
		
		long start = System.nanoTime();
		
		for(int i = 0; i<timesToRun; i++)
		{
			try {
				process.invoke(null, data[0]);
			} catch (Exception e) {
				//Triggers if an error is encountered once the benchmark is started
				return -1;
			}
		}
		long end = System.nanoTime();
		
		//Return the time taken in nanoseconds
		return end-start;
	}

	public final ArrayList<Object> execute()
	{
		ArrayList<Object> outData = new ArrayList<Object>();
		
		//Currently only works if there is one parameter...
		Class<?> paramType = this.process.getParameterTypes()[0];
		
		//Working with primitives suck. Probably going to deprecate
		if(ClassUtils.isPrimitiveOrWrapper(paramType))
		{
			if(paramType.isPrimitive())
			{
				paramType = ClassUtils.primitiveToWrapper(paramType);
			}
		}
		//Calls process on each piece of data
		for(Object datum : this.data)
		{
			try {
				outData.add(
						this.process.invoke(null,
								paramType.cast(datum)));
			} catch (Exception e)
			{
				//If shit goes insane
				e.printStackTrace();
			}
		}
		return outData;
	}

	//Receives data and then does data validation. Must have a process already defined
	public boolean receiveData(Object[] data)
	{
		Class<?> inType = this.process.getParameterTypes()[0];
		if(!(inType.isInstance(data[0])))
			return false;
		this.data = data;
		return true;
	}

	//Just defines the process
	public boolean setProcess(Method process)
	{
		this.process = process;
		return true;
	}

}