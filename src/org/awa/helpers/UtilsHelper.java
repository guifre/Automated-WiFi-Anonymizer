package org.awa.helpers;

import java.util.Random;
/**
 * 
 * UtilsHelper provides a set of generic functionalities for WifiGhoster
 *
 */
public enum UtilsHelper {
	
	INSTANCE;
	
	private static final String[] CHARS = {"A", "B", "C", "D", "E", "F", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"}; 
	private static final int GROUPS = 6;
	private static final int DIGITS = 2;
	
	private static Random rand = new Random();
	
	@SuppressWarnings("static-method")
	public String getRandMac() {
		String mac = "";
		for (int i = 0; i < UtilsHelper.GROUPS; i++) {
			for (int j = 0; j < UtilsHelper.DIGITS; j++) {
				mac = mac.concat(UtilsHelper.CHARS[UtilsHelper.rand.nextInt(UtilsHelper.CHARS.length)]);
			}
			if (i < UtilsHelper.GROUPS-1) {
				mac = mac.concat(":");
			}
		}
		return mac;
	}

}
