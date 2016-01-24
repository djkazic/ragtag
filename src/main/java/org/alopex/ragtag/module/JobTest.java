package org.alopex.ragtag.module;

import java.util.ArrayList;

import junit.framework.TestCase;

public class JobTest extends TestCase {

	//Failure test class
	public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException {

		ArrayList<Object> nums = new ArrayList<Object> ();
		for(int i = 0; i < 10; i++)
			nums.add(i);

		//Transparent
		Job job = new Job(nums, Process.class);
		
	}
}
