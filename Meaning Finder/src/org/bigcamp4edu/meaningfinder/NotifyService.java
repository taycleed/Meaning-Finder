package org.bigcamp4edu.meaningfinder;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class NotifyService extends Service {

	static final int NOTIFICATION_ID = 1;
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("VOM", "noti service started");
		
		String title = getString(R.string.app_name); 
        String text = getString(R.string.noti_message);
        sendNotification(title, text);
        
        Log.d("VOM", "noti service set noti");
        
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void sendNotification(String title, String text)
	{
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_launcher);
		Intent clickIntent = new Intent(this, QuestionActivity.class);
	    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	    Notification notification = new NotificationCompat.Builder(this)
	    		.setTicker(text)
		    	.setContentTitle(title)
		    	.setContentText(text)
		    	.setSmallIcon(R.drawable.icon_launcher)
		    	.setContentIntent(pendingIntent)
		    	.setLargeIcon(bitmap).getNotification();

	    notification.flags = Notification.FLAG_AUTO_CANCEL;

	    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	    manager.notify(NOTIFICATION_ID, notification);
	    
	    /////////////////////////////////////
	    
	    Intent intent = new Intent(this, DialogActivity.class);
	    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    startActivity(intent);
	    
	}
}
