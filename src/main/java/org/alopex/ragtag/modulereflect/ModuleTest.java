package org.alopex.ragtag.modulereflect;

import java.util.ArrayList;

import junit.framework.TestCase;

public class ModuleTest extends TestCase {
	
	//Failure test class
	public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, NullProcessException {

		Class<?> clazz = Process.class;
		Double[] nums = new Double[10];
		for(int i = 0; i < nums.length; i++)
			nums[i] = Double.parseDouble(i + "");
		
		//Transparent
		Module module = new Module();
		module.setProcess(Process.class.getMethods()[0]);
		if(module.receiveData(nums)) {
			System.out.println(module.benchmark(10));
			ArrayList<Object> output = module.execute();
			System.out.println(output.size());
			for(int i = 0; i < nums.length; i++) {
				System.out.println((i % 3 + " " + 
			(clazz.getMethods()[0]
					.getReturnType().
					cast(output.get(i)))));
			}
		}
	}
}
