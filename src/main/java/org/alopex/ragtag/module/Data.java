package org.alopex.ragtag.module;

public class Data<T> {

	T value;
	private final Class<T> type;
	private Processor<?> processor;
	
	public Data(Class<T> type, T value) {
		this.type = type;
		this.value = value;
	}
	
	public void setProcess(Processor<T> proc) {
		processor = proc;
	}
	
	public Object process() {
		return processor.process();
	}
	
	public Class<T> getType() {
		return type;
	}
}
