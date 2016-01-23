package org.alopex.ragtag.module;

public class TestModule extends Module<Double> {

	@Override
	public void define() {
		this.setProcessor(new Processor<Double>() {
			public Object process() {
				for(int i=0; i < 255; i++) {
					data = Math.pow(10D, data);
				}
				return data;
			}
		});
	}
	
	@Override
	public void read() {
		for(int i=0; i < 20; i++) {
			System.out.println("Calling insertData for " + i);
			insertData(Double.parseDouble(i + ""));
		}
	}
	
	//Handled code here
	public static void main(String[] args) {
		TestModule tm = new TestModule();
		tm.define();
		tm.read();
		System.out.println(tm.benchmark() + " ns");
	}
}
