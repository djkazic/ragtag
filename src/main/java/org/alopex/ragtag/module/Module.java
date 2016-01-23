package org.alopex.ragtag.module;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Module<T> {
	
	private ArrayList<Data<T>> dataList = new ArrayList<Data<T>> ();
	private HashMap<Data<T>, Object> output = new HashMap<Data<T>, Object> ();
	private Processor<T> processor;
	
	public abstract void define();
	public abstract void read();
	
	public void setProcessor(Processor<T> processor) {
		this.processor = processor;
	}
	
	//Inserts an atomic piece of data
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void insertData(T o) {
		Data data = new Data(o.getClass(), o);
		processor.data = (T) o;
		data.setProcess(processor);
		dataList.add(data);
		System.out.println("Inserted data: " + data.process());
	}
	
	//Iterates through dataList until it is empty, saving all outputs
	public void execute() {
		while(dataList.size() > 0) {
			Data<T> data = dataList.remove(0);
			if(data != null) {
				output.put(data, data.process());
			}
		}
	}
	
	//Times how long one iteration of this process takes
	public long benchmark() {
		//Throw out 5 iterations to stabilize benchmark timing
		for(int i=0; i < 5; i++) {
			dataList.get(0).process();
		}
		long startTime = System.nanoTime();
		dataList.get(0).process();
		long endTime = System.nanoTime();
		return endTime - startTime;
	}
}
