package org.awa;

import org.awa.devices.SysCallException;
import org.awa.devices.impl.SysCallManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiReceiver extends BroadcastReceiver {

	public static boolean randomizable = false;
	private static MainActivity myActivity;

	public static void setActivity(MainActivity a) {
		WifiReceiver.myActivity = a;
	}

	public static WifiInfo getCurrentWifiInfo(Context context) {
		final ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (networkInfo != null && networkInfo.isConnected()) {
			final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			return wifiManager.getConnectionInfo();
		}

		return null;
	}
	
	
	@SuppressWarnings("static-method")
	private void randomize() {
		if(randomizable == true) {
			try {
				SysCallManager.INSTANCE.renewAddr();
				WifiReceiver.myActivity.showCurrentMac();
			} catch (SysCallException e) {
				Log.d("MAC address renoval", "Could not renew MAC Address " + e.getMessage());
			}
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("wifireceiver", "checking wifi state...");

		String action = intent.getAction();
		if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
			//
		} else if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
			int iTemp = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
			checkState(iTemp);
		} else if (action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
			WifiInfo WifiInfo = getCurrentWifiInfo(context);
			@SuppressWarnings("static-access")
			DetailedState state = WifiInfo.getDetailedStateOf((SupplicantState) intent.getParcelableExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED));
			changeState(state);
		} else if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
			DetailedState state = ((NetworkInfo) intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO)).getDetailedState();
			changeState(state);
		}
	}

	private void changeState(DetailedState aState) {
		if (aState == DetailedState.SCANNING) {
			Log.d("wifiSupplicanState", "SCANNING");
			//this.randomize();
		} else if (aState == DetailedState.CONNECTING) {
			Log.d("wifiSupplicanState", "CONNECTING");
		} else if (aState == DetailedState.OBTAINING_IPADDR) {
			Log.d("wifiSupplicanState", "OBTAINING_IPADDR");
		} else if (aState == DetailedState.CONNECTED) {
			Log.d("wifiSupplicanState", "CONNECTED");
		} else if (aState == DetailedState.DISCONNECTING) {
			Log.d("wifiSupplicanState", "DISCONNECTING");
			this.randomize();
		} else if (aState == DetailedState.DISCONNECTED) {
			Log.d("wifiSupplicanState", "DISCONNECTTED");
			//

		} else if (aState == DetailedState.FAILED) {
			//
		}
	}

	public void checkState(int aInt) {
		if (aInt == WifiManager.WIFI_STATE_ENABLING) {
			Log.d("WifiManager", "WIFI_STATE_ENABLING");
			this.randomize();
		} else if (aInt == WifiManager.WIFI_STATE_ENABLED) {
			Log.d("WifiManager", "WIFI_STATE_ENABLED");
		} else if (aInt == WifiManager.WIFI_STATE_DISABLING) {
			Log.d("WifiManager", "WIFI_STATE_DISABLING");
			this.randomize();
		} else if (aInt == WifiManager.WIFI_STATE_DISABLED) {
			Log.d("WifiManager", "WIFI_STATE_DISABLED");
		}
	}
}


