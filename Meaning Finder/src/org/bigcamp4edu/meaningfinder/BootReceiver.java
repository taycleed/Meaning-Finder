package org.bigcamp4edu.meaningfinder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context ctxt, Intent i) {
		// Re-set alarms right after device reboot.
		Log.d("VOM BootReceiver", "Reset alarms");
		SetupActivity.UpdateAlarm(ctxt.getApplicationContext(), 0);
	}

}
