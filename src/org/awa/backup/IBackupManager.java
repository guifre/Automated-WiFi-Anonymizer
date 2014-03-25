package org.awa.backup;

import org.awa.devices.SysCallException;

/**
 * 
 * IBackupManager defines the methods to be used to persist
 * and recover the MAC Address of the wireless device
 */
public interface IBackupManager {
	
	/**
	 * Should be invoked when ghostery is exectued
	 * checks if there is backup, and creates one otherwise
	 * @throws SysCallException 
	 */
	public void startUpOps() throws SysCallException;
	
	
	
	/**
	 * 
	 * @return the backedup mac addr
	 * @throws SysCallException 
	 */
	public String getMacAddr() throws SysCallException;

}
