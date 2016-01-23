package org.alopex.ragtag.modulereflect;

import java.util.ArrayList;

import junit.framework.TestCase;

public class ModuleTest extends TestCase {
	
	public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException
	{

		Class<?> clazz = new Process().getClass();
		Integer[] nums = new Integer[10];
		for(int i = 0; i < nums.length; i++)
			nums[i] = i;
		Module module = new Module();
		module.setProcess(new Process().getClass().getMethods()[0]);
		module.receive(nums);
		System.out.println(module.benchmark() / 100000.0);
		ArrayList<Object> output = module.execute();
		System.out.println(output.size());
		for(int i = 0; i < nums.length; i++)
		{
			System.out.println((i % 3 + " " + 
		(clazz.getMethods()[0]
				.getReturnType().
				cast(
						output.get(i)))));
		}
	}
}
