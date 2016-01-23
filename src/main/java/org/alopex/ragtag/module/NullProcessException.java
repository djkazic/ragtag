package org.alopex.ragtag.module;

public class NullProcessException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 404L;
	
	private String errorCode = "Null_Process_Exception";
	
	public NullProcessException(String message) {
		super(message);
	}
	
	public String getErrorCode()
	{
		return this.errorCode;
	}
}
