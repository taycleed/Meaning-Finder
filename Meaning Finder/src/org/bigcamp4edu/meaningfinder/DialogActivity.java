package org.bigcamp4edu.meaningfinder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.WindowManager;

public class DialogActivity extends Activity {
	
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		
		new AlertDialog.Builder(this)
	    	.setIcon(R.drawable.icon_launcher)
	    	.setMessage(R.string.noti_message)
	    	.setTitle(R.string.app_name)
	    	.setPositiveButton(R.string.noti_yes, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) {
	            	Intent intent = new Intent(DialogActivity.this, LogoActivity.class);
	            	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
	            	startActivity(intent);
	            	
	            	NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	            	manager.cancel(NotifyService.NOTIFICATION_ID);
	                
	            	finish();
	            }
	        })
	        .setNegativeButton(R.string.noti_no, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) {
	            	moveTaskToBack(true);
//	            	Intent intent = new Intent(Intent.ACTION_MAIN);
//	            	intent.addCategory(Intent.CATEGORY_HOME);
//	            	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	            	startActivity(intent);

	            	finish();
	            }
	        })
	        .show();
	}
}
