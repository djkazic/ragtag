package org.alopex.ragtag;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

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

	public static String SHAsum(byte[] convertme) throws NoSuchAlgorithmException{
		MessageDigest md = MessageDigest.getInstance("SHA-1"); 
		return byteArray2Hex(md.digest(convertme));
	}
	
	private static String byteArray2Hex(final byte[] hash) {
	    Formatter formatter = new Formatter();
	    for(byte b : hash) {
	        formatter.format("%02x", b);
	    }
	    String output = formatter.toString();
	    formatter.close();
	    return output;
	}
}
