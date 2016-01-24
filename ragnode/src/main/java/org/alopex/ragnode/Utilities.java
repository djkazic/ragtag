package org.alopex.ragnode;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Random;

import org.restlet.engine.util.Base64;

import com.esotericsoftware.minlog.Log;

public class Utilities {

	public static void log(Object someClass, String msg, boolean debug) {
		String output = "[" + someClass.getClass().getName() + "]: " + msg;
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

	public static String randomMACAddress(){
		Random rand = new Random();
		byte[] macAddr = new byte[6];
		rand.nextBytes(macAddr);
		macAddr[0] = (byte)(macAddr[0] & (byte) 254);
		StringBuilder sb = new StringBuilder(18);
		for(int i=0; i < macAddr.length; i++){
			sb.append(String.format("%02X%s", macAddr[i], (i < macAddr.length - 1) ? "-" : ""));
		}
		return sb.toString();
	}

	public static String base64(String input) {
		return Base64.encode(input.getBytes(), false);
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
