package org.awa.backup.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.awa.backup.IBackupManager;
import org.awa.devices.SysCallException;
import org.awa.devices.impl.SysCallManager;

import android.os.Environment;
/**
 * BackupManger in in charge of persisting and recovering 
 * the original MAC Address of the targeted interface
 */
public enum BackupManger implements IBackupManager {

	INSTANCE;
	
	private File mBackUpFile;
	private final String mFilename = "macaddrback";
	
	@Override
	public void startUpOps() throws SysCallException {
		File path = Environment.getExternalStorageDirectory();
		this.mBackUpFile = new File(path, "/" + this.mFilename);
		if (!this.mBackUpFile.exists()) {
			createBackUp();
		}
	}
	
	/**
	 * Dumps the device MAC Address into the backupfile
	 * @throws SysCallException
	 */
	private void createBackUp() throws SysCallException {
		BufferedWriter bw = null;
		try {

			FileWriter fw = new FileWriter(this.mBackUpFile.getAbsoluteFile());
 			if (!this.mBackUpFile.exists()) {
 				this.mBackUpFile.createNewFile();
			}
			bw = new BufferedWriter(fw);
			bw.write(SysCallManager.INSTANCE.getCurrentAddr());
		} catch (IOException e) {
			throw new SysCallException("Could not create backup file "+e.getMessage());
		} finally {
			if (bw != null)
				try {
					bw.close();
				} catch (IOException e) {
					throw new SysCallException("Could not create backup file "+e.getMessage());
				} 
		}
	}


	/**
	 * Reads the MAC Address from the backupfile
	 */
	@Override
	public String getMacAddr() throws SysCallException {
		BufferedReader br = null;
		try {
			String mac;
			br = new BufferedReader(new FileReader(this.mBackUpFile));
			if ((mac = br.readLine()) != null) {
				return mac;
			}
		} catch (IOException e) {
			throw new SysCallException("Could not read file "+e.getMessage());
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException e) {
				throw new SysCallException("Could not read file "+e.getMessage());
			}
		}
		throw new SysCallException("Could not read file"); 
	}
}
