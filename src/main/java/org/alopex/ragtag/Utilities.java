package org.alopex.ragtag;

import com.esotericsoftware.minlog.Log;

public class Utilities {

	public static void log(Object someClass, String msg, boolean debug) {
		String output = "[" + someClass.getClass().getSimpleName() + "]: " + msg;
		if(debug) {
			Log.debug(output);
		} else {
			Log.info(output);
		}
	}
	
	public static void log(String someClass, String msg, boolean debug) {
		String output = "[" + someClass + "]: " + msg;
		if(debug) {
			Log.debug(output);
		} else {
			Log.info(output);
		}
	}
}
