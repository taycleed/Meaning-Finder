package org.bigcamp4edu.meaningfinder;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.Vibrator;
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
	
	public static boolean HasSavedToday(Context context){
		long lastSaveTime = context.getSharedPreferences("Setting", 0).getLong(DB.LAST_ANSWER_DATE, 0);
		if(lastSaveTime != 0){
			Calendar calendar = new GregorianCalendar();	// 현재 시각
			Calendar calendar2 = new GregorianCalendar();
			calendar2.setTimeInMillis(lastSaveTime);		// 마지막 답변한 시각
			
			Log.d("VOM NotifyService", "NOW: " + String.format("%4d/%2d/%2d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) +1, calendar.get(Calendar.DATE)));
			Log.d("VOM NotifyService", "SAVED: " + String.format("%4d/%2d/%2d", calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH) +1, calendar2.get(Calendar.DATE)));
			
			return calendar.get(Calendar.DATE) == calendar2.get(Calendar.DATE) 
				&& calendar.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) 
				&& calendar.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) ;
		}
		
		return false;
	}
	
	private void sendNotification(String title, String text)
	{
		// 알람 TriggerTime 재설정
		SetupActivity.UpdateAlarm(NotifyService.this, 1);
		
		if(HasSavedToday(this)){
			// 오늘 답변을 했으므로 질문 알림을 패스한다. 
			return ;
		}
		
		// Vibrate
		Vibrator vib = (Vibrator)	getSystemService(Context.VIBRATOR_SERVICE);
		SharedPreferences pref = getSharedPreferences("Setting", 0);
		if(vib.hasVibrator() && pref.getBoolean("Vibrate", true)){
			vib.vibrate(500);
		}
		
		// Notification Bar에 표시할 Notification setting
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_launcher);
		Intent clickIntent = new Intent(this, QuestionActivity.class);
	    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	    @SuppressWarnings("deprecation")
		Notification notification = new NotificationCompat.Builder(this)
	    		.setTicker(text)
		    	.setContentTitle(title)
		    	.setContentText(text)
		    	.setSmallIcon(R.drawable.icon_launcher)
		    	.setContentIntent(pendingIntent)
		    	.setLargeIcon(bitmap).getNotification();

	    notification.flags = Notification.FLAG_AUTO_CANCEL;

	    // Nofitication bar에 Notification 등록
	    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	    manager.notify(NOTIFICATION_ID, notification);
	    
	    /////////////////////////////////////
	    
	    // 전면 Dialog 알람 시작
	    Intent intent = new Intent(this, DialogActivity.class);
	    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    startActivity(intent);
	}
}
