package org.awa;

import org.awa.backup.impl.BackupManger;
import org.awa.devices.SysCallException;
import org.awa.devices.impl.SysCallManager;
import org.wifighoster.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		WifiReceiver.setActivity(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.addListenerOnButton();
		
		this.loadAd();
		
		this.checkSuperCow();

		this.showOriginalMac();

		this.showCurrentMac();

		this.showSwitch();
	}
	
	private void showSwitch() {
		Switch onOffSwitch = (Switch) findViewById(R.id.switch1);
		try {
			if (BackupManger.INSTANCE.getMacAddr() != SysCallManager.INSTANCE
					.getCurrentAddr()) {
				onOffSwitch.setChecked(true);
				WifiReceiver.randomizable = true;
			}
		} catch (SysCallException e1) {
			getAlert("MAC Address Backup", "Could not restore the backup MAC Address\n" + e1.getMessage());
		}
		onOffSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Log.v("Switch State=", "" + isChecked);
				if (isChecked) {
					WifiReceiver.randomizable = true;
				} else {
					try {
						WifiReceiver.randomizable = false;
						SysCallManager.INSTANCE.restoreAddr();
						showCurrentMac();
					} catch (SysCallException e) {
						getAlert("MAC Address Backup", "Could not restore the backup MAC Address\n" + e.getMessage());
					}
				}
			}

		});
	}
	
	private void checkSuperCow() {
		try {
			if (SysCallManager.INSTANCE.isSuperCow()) {
				this.getAlert("Super Cow Powers", "Super Cow Powers Acquired");
			} else {
				this.getAlert("Super Cow Powers","Could not acquire Super Cow Powers. Root your phone");
			}
		} catch (SysCallException e) {
			this.getAlert("Super Cow Powers", "Could not acquire Super Cow Powers. Root your phone\n"+ e.getMessage());
		}
	}
	
	private void loadAd() {
		AdView adView = (AdView) findViewById(R.id.ad);
		adView.loadAd(new AdRequest());
	}

	public void showOriginalMac() {
		try {
			BackupManger.INSTANCE.startUpOps();
			TextView originalmac = (TextView) findViewById(R.id.originalmac);
			originalmac.setText(BackupManger.INSTANCE.getMacAddr());
		} catch (SysCallException e) {
			this.getAlert( "MAC Address Backup", "Could not Backup the current MAC Address\n" + e.getMessage());
		}
	}
	
	public void showCurrentMac() {
		
		try {
			TextView currentMac = (TextView) findViewById(R.id.currentmac);
			currentMac.setText(SysCallManager.INSTANCE.getCurrentAddr());
		} catch (SysCallException e) {
			this.getAlert("MAC Address", "Could not read the current MAC Address\n" + e.getMessage());
		}

	}

	public void addListenerOnButton() {
		Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try {
					SysCallManager.INSTANCE.restoreAddr();
					showCurrentMac();
				} catch (SysCallException e) {
					getAlert("MAC Address", "Could not restore MAC Address\n" + e.getMessage());
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	AlertDialog getAlert(String title, String msg) {
		return new AlertDialog.Builder(this).setTitle(title).setMessage(msg).setPositiveButton(android.R.string.ok, null).show();
	}

	public void createIfaceEditor() {
		EditText editText = (EditText) findViewById(R.id.editText1);
		editText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
						|| actionId == EditorInfo.IME_ACTION_DONE) {
					Log.e("MyApp", " ------> IN EDITOR ACTION DONE");
					SysCallManager.INSTANCE.setIFace(v.getText().toString());
				}
				return false;
			}
		});
	}
}
