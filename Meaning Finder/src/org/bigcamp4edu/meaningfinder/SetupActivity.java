package org.bigcamp4edu.meaningfinder;

import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import android.R.string;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

public class SetupActivity extends Activity {

	private SharedPreferences pref;
	
	private TextView one_alarm_time;
	private LinearLayout one_alarm_wrap;
	private String one_time;

	private TextView two_alarm_time;
	private LinearLayout two_alarm_wrap;
	private String two_time;

	private int mHour;
	private int mMinute;
	
	private int mHourTwo;
	private int mMinuteTwo;

	static final int one_TIME_DIALOG_ID = 0;
	static final int two_TIME_DIALOG_ID = 1;
	static final int oneDay = 24 * 60 * 60 * 1000;
	
	PendingIntent pendingI,pendingI_two;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE); // 액션바 제거
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN); // 상태바 제거
		
		if(pendingI == null){
			setPendingIntent();
		}

		setContentView(R.layout.activity_setup);

		// capture our View elements
		one_alarm_time = (TextView) findViewById(R.id.one_alarm_time);
		one_alarm_wrap = (LinearLayout) findViewById(R.id.one_alarm_wrap);

		one_alarm_wrap.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(one_TIME_DIALOG_ID);
			}
		});

		// capture our View elements
		two_alarm_time = (TextView) findViewById(R.id.two_alarm_time);
		two_alarm_wrap = (LinearLayout) findViewById(R.id.two_alarm_wrap);

		two_alarm_wrap.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(two_TIME_DIALOG_ID);
			}
		});

		pref 					= getSharedPreferences("Setting", 0);
		String one_alarm		= pref.getString("one_alarm", "noset");
		int one_alarm_hour		= pref.getInt("one_alarm_hour", 0);
		int one_alarm_minute	= pref.getInt("one_alarm_minute", 0);
		
		String two_alarm		= pref.getString("two_alarm", "noset");
		int two_alarm_hour		= pref.getInt("two_alarm_hour", 0);
		int two_alarm_minute	= pref.getInt("two_alarm_minute", 0);
		
		if(one_alarm.equals("set")){
			mHour 	= one_alarm_hour;
			mMinute = one_alarm_minute;
			updateDisplay(1);
		}
		
		if(two_alarm.equals("set")){
			mHourTwo	= two_alarm_hour;
			mMinuteTwo 	= two_alarm_minute;
			updateDisplay(2);
		}
		
		Button btn_close = (Button) findViewById(R.id.setting_btn);
		btn_close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		((Button) findViewById(R.id.btn_logout)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SetupActivity.this, NotifyService.class);
				Log.d("VOM", "Force Start Service");
				startService(intent);
			}
		});
		
	}

	/*
	 * Update TextView which shows alarm time.
	 */
	private void updateDisplay(int id) {
		if(id == 1)
		{
			int setHour = mHour;

			if (mHour < 12) {
				one_time = "오전";
			} else {
				one_time = "오후";
				setHour = setHour - 12;
			}
			
			one_alarm_time.setText(new StringBuilder().append(one_time + " ")
				.append(String.format("%02d", setHour)).append(":").append(String.format("%02d", mMinute)));
			
			pref = getSharedPreferences("Setting", 0);
			SharedPreferences.Editor edit	= pref.edit();
			edit.putString("one_alarm", "set");
			edit.putInt("one_alarm_hour", mHour);
			edit.putInt("one_alarm_minute", mMinute);
			edit.commit();
			
		}else if(id == 2){
			int setHour = mHourTwo;

			if (mHourTwo < 12) {
				two_time = "오전";
			} else {
				two_time = "오후";
				setHour = setHour - 12;
			}
			
			two_alarm_time.setText(new StringBuilder().append(two_time + " ")
				.append(String.format("%02d", setHour)).append(":").append(String.format("%02d", mMinuteTwo)));
			
			pref = getSharedPreferences("Setting", 0);
			SharedPreferences.Editor edit	= pref.edit();
			edit.putString("two_alarm", "set");
			edit.putInt("two_alarm_hour", mHourTwo);
			edit.putInt("two_alarm_minute", mMinuteTwo);
			edit.commit();
		}
	}
	
	private static long getTriggerTime(int hour, int minute)
	{
	    GregorianCalendar calendar = new GregorianCalendar();
	    calendar.set(GregorianCalendar.HOUR_OF_DAY, hour);
	    calendar.set(GregorianCalendar.MINUTE, minute);
	    calendar.set(GregorianCalendar.SECOND, 0);
	    calendar.set(GregorianCalendar.MILLISECOND, 0);

	    if (calendar.before(new GregorianCalendar()))
	    {
	        calendar.add(GregorianCalendar.DAY_OF_MONTH, 1);
	    }

	    return calendar.getTimeInMillis();
	}
	
	private void setPendingIntent(){
		//사용자가 알람을 확인하고 클릭했을때 새로운 액티비티를 시작할 인텐트 객체
		Intent intent = new Intent(SetupActivity.this, NotifyService.class);
		
		Intent intent2 = new Intent(SetupActivity.this, QuestionActivity.class);
		//인텐트 객체를 포장해서 전달할 인텐트 전달자 객체
		pendingI = PendingIntent.getService(SetupActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		pendingI_two = PendingIntent.getActivity(SetupActivity.this, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			updateDisplay(1);
			
			Log.d("VOM", "Time Set(1)");
			
			AlarmManager alarmManager = (AlarmManager) SetupActivity.this.getSystemService(Context.ALARM_SERVICE);
			alarmManager.cancel(pendingI);
			alarmManager.setRepeating(AlarmManager.RTC, getTriggerTime(mHour, mMinute), oneDay, pendingI);
		}
	};
	
	private TimePickerDialog.OnTimeSetListener mTimeSetListenerTwo = new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHourTwo = hourOfDay;
			mMinuteTwo = minute;
			updateDisplay(2);
			
			Log.d("VOM", "Time Set(2)");
			
			AlarmManager alarmManager = (AlarmManager) SetupActivity.this.getSystemService(Context.ALARM_SERVICE);
			alarmManager.cancel(pendingI_two);
			alarmManager.setRepeating(AlarmManager.RTC, getTriggerTime(mHourTwo, mMinuteTwo), oneDay, pendingI_two);
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		if(id == one_TIME_DIALOG_ID){
			return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute,	false);
		}else if(id == two_TIME_DIALOG_ID){
			return new TimePickerDialog(this, mTimeSetListenerTwo, mHourTwo, mMinuteTwo, false);
		}
		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setup, menu);
		return true;
	}

}
