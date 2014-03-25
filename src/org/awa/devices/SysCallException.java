package org.awa.devices;

/**
 * SysCallException is the exception to be used when handling
 * system call related issues within wifighoster
 *
 */
public class SysCallException extends Exception {

	private static final long serialVersionUID = -2719076953781198611L;
	
	public SysCallException() {
		 super();
	}
	
	public SysCallException(String message) {
		super(message);
	}
	
}
