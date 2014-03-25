package org.awa.devices.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.awa.backup.impl.BackupManger;
import org.awa.devices.ISysCallManager;
import org.awa.devices.SysCallException;
import org.awa.helpers.UtilsHelper;

import android.util.Log;

public enum SysCallManager implements ISysCallManager {

	INSTANCE;
	
	private final String SU = "su";
	private final String ENCODING = "ASCII";
	private final String IP_LINK = "ip link";
	private final String SHOW = "show";
	private final String SET = "set";
	private final String ADDRESS = "address";
	private final String ID = "id";
	private final String TOOR = "uid=[^\\(]";
	private final String GREP = "grep -Eo";
	private final String SPACE = " ";
	private final String PIPE = "|";
	private final String MAC_REGEX = "..\\(\\:..\\)\\{5\\}[^ff:ff:ff:ff:ff]";
	private String iface;

	
	private SysCallManager() {
		this.iface = "wlan0";
	}
	
	@Override
	public void setIFace(String newIface) {
		this.iface = newIface;		
	}
	
	@Override
	public String getCurrentAddr() throws SysCallException {
		//$ip link show wlan0|grep ..\\(\\:..\\)\\{5\\}[^ff:ff:ff:ff:ff]
		return this.runCmd(this.IP_LINK + this.SPACE +this.SHOW + this.SPACE + this.iface + this.PIPE + this.GREP + this.SPACE + this.MAC_REGEX);
	}

	@Override
	public void renewAddr() throws SysCallException {
		this.runCmd(this.getNewAddrCmd(UtilsHelper.INSTANCE.getRandMac()));
	}

	@Override
	public void restoreAddr() throws SysCallException {
		this.runCmd(this.getNewAddrCmd(BackupManger.INSTANCE.getMacAddr()));
	}

	@Override
	public boolean isSuperCow() throws SysCallException {
		String supercow = this.runCmd(this.getIsCowCmd());
		Log.d("supercow", "supercow cmd returned " + supercow);
		return supercow.endsWith("uid=0");
	}
	private String getIsCowCmd() {
		//$id|grep -Eo uid=[^\(]"
		return this.ID + this.PIPE + this.GREP + this.SPACE + this.TOOR;
	}

	private String getNewAddrCmd(String in) {
		//$ip link set wlan0 address ff:ff:ff:ff:ff:ff 
		return this.IP_LINK + this.SPACE + this.SET + this.SPACE + this.iface + this.SPACE + this.ADDRESS + this.SPACE + in;
	}

	private String runCmd(String cmd) throws SysCallException {
		Process sh = null;
		try {
			Log.d("syscall", "running " + cmd);
			sh = Runtime.getRuntime().exec(this.SU, null, null);
			OutputStream os = sh.getOutputStream();
			os.write((cmd).getBytes(this.ENCODING));
			os.flush();
			os.close();
			sh.waitFor();
			if (sh.exitValue() != 0) {
				throw new SysCallException("Could not execute " + cmd
						+ this.getStringFromInputStream(sh.getInputStream())
								.concat(this.getStringFromInputStream(sh.getErrorStream())));
			}
			return this.getStringFromInputStream(sh.getInputStream());
		} catch (IOException e) {
			throw new SysCallException("Could not execute " + cmd + "\n" + e.getMessage());
		} catch (InterruptedException e) {
			throw new SysCallException("Could not execute " + cmd + "\n" + e.getMessage());
		} finally {
			if (sh != null) {
				try {
					sh.getOutputStream().close();
					sh.getInputStream().close();
					sh.getErrorStream().close();
				} catch (IOException e) {
					//
				}
			}
		}
	}

	@SuppressWarnings("static-method")
	private String getStringFromInputStream(InputStream is) throws SysCallException {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			throw new SysCallException("could not read inputstream "+e.getMessage());
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					throw new SysCallException("could not read inputstream " + e.getMessage());
				}
			}
		}
		return sb.toString();
	}
}
