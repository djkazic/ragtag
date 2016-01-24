package org.alopex.ragtag.module;

public abstract class Process {

    //Sample user defined method	
	public static Boolean process(Integer hello) {
		int out = 0;
		for(int i=0; i < hello; i++) {
			out += i;
		}
		return out % 5 == 0;
	}
}
