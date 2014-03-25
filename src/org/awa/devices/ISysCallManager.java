package org.awa.devices;

/**
 * ISysCallManager defines the set of methods to be used by wifighoster
 * for interacting with the operating system
 */
public interface ISysCallManager {
	
	/**
	 * Replaces the current MAC addr with a new random generated one
	 * @throws SysCallException 
	 */
	public void renewAddr() throws SysCallException;
	
	/**
	 * Replaces the current MAC addr with a new random generated one
	 * @throws SysCallException 
	 */
	public void restoreAddr() throws SysCallException;

	/**
	 * @return the current mac addr
	 * @throws SysCallException 
	 */
	public String getCurrentAddr() throws SysCallException;
	
	
	/**
	 * @return whether the phone is rooted
	 * @throws SysCallException 
	 */
	public boolean isSuperCow() throws SysCallException;
	
	/**
	 * Changes the default targeted interface
	 */
	public void setIFace(String newIface);
	

}
