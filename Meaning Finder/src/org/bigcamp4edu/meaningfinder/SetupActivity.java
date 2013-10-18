package org.bigcamp4edu.meaningfinder;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

public class SetupActivity extends Activity {

	private SharedPreferences pref;
	
	private TextView one_alarm_time;
	private LinearLayout one_alarm_wrap;
	private boolean one_onoff;

	private TextView two_alarm_time;
	private LinearLayout two_alarm_wrap;
	private boolean two_onoff;

	private int mHour;
	private int mMinute;
	
	private int mHourTwo;
	private int mMinuteTwo;

	static final int one_TIME_DIALOG_ID = 0;
	static final int two_TIME_DIALOG_ID = 1;
	static final int oneDay = 24 * 60 * 60 * 1000;
	
	AlarmManager alarmManager ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE); // 액션바 제거
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN); // 상태바 제거
		
		setContentView(R.layout.activity_setup);
		alarmManager = (AlarmManager) SetupActivity.this.getSystemService(Context.ALARM_SERVICE);

		pref 					= getSharedPreferences("Setting", 0);
		one_onoff				= pref.getString("one_alarm", "set").equals("set");
		int one_alarm_hour		= pref.getInt("one_alarm_hour", 7);
		int one_alarm_minute	= pref.getInt("one_alarm_minute", 0);
		
		two_onoff				= pref.getString("two_alarm", "set").equals("set");
		int two_alarm_hour		= pref.getInt("two_alarm_hour", 23);
		int two_alarm_minute	= pref.getInt("two_alarm_minute", 0);
		
		// capture our View elements
		one_alarm_time = (TextView) findViewById(R.id.one_alarm_time);
		one_alarm_wrap = (LinearLayout) findViewById(R.id.one_alarm_wrap);

		one_alarm_wrap.setOnClickListener(new View.OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				showDialog(one_TIME_DIALOG_ID);
			}
		});

		// capture our View elements
		two_alarm_time = (TextView) findViewById(R.id.two_alarm_time);
		two_alarm_wrap = (LinearLayout) findViewById(R.id.two_alarm_wrap);

		two_alarm_wrap.setOnClickListener(new View.OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				showDialog(two_TIME_DIALOG_ID);
			}
		});
		
		((CheckBox) findViewById(R.id.checkBox_setting_time1)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					one_alarm_wrap.setClickable(true);
					one_alarm_wrap.setAlpha(1.0f);
					pref.edit()
						.putString("one_alarm", "set")
						.commit();
				}else{
					one_alarm_wrap.setClickable(false);
					one_alarm_wrap.setAlpha(0.5f);
					pref.edit()
					.putString("one_alarm", "noset")
					.commit();
				}
				one_onoff = isChecked;
				updateDisplay(1);
				UpdateAlarm(SetupActivity.this, 0);
			}
		});
		((CheckBox) findViewById(R.id.checkBox_setting_time2)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					two_alarm_wrap.setClickable(true);
					two_alarm_wrap.setAlpha(1.0f);
					pref.edit()
					.putString("two_alarm", "set")
					.commit();
				}else{
					two_alarm_wrap.setClickable(false);
					two_alarm_wrap.setAlpha(0.5f);
					pref.edit()
					.putString("two_alarm", "noset")
					.commit();
				}
				two_onoff = isChecked;
				updateDisplay(2);
				UpdateAlarm(SetupActivity.this, 0);
			}
		});
		((CheckBox) findViewById(R.id.checkBox_setting_vibrate)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				updateVibrateInfo(isChecked);
			}
		});
		
		mHour 	= one_alarm_hour;
		mMinute = one_alarm_minute;
		((CheckBox) findViewById(R.id.checkBox_setting_time1)).setChecked(one_onoff);
		updateDisplay(1);
		
		mHourTwo	= two_alarm_hour;
		mMinuteTwo 	= two_alarm_minute;
		((CheckBox) findViewById(R.id.checkBox_setting_time2)).setChecked(two_onoff);
		updateDisplay(2);
		
		((CheckBox) findViewById(R.id.checkBox_setting_vibrate)).setChecked(pref.getBoolean("Vibrate", true));
		updateVibrateInfo(pref.getBoolean("Vibrate", true));
		
		Button btn_close = (Button) findViewById(R.id.setting_btn);
		btn_close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		// 로그아웃 버튼
		((Button) findViewById(R.id.btn_logout)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(SetupActivity.this, NotifyService.class);
//				Log.d("VOM", "Force Start Service");
//				startService(intent);
				
				SharedPreferences.Editor prefEditor = getSharedPreferences("Setting", 0).edit();
				prefEditor.putString("userId", "");
				prefEditor.putString("userPw", "");
				prefEditor.putBoolean("LOGIN_STATE", false);
				prefEditor.commit();
				
				Var.userId		= "";
				Var.userPw		= "";
				Var.LOGIN_STATE	= false;
				
				Intent intent = new Intent(SetupActivity.this, LogoActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);	
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
				finish();	// Setup 액티비티 닫기
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	private void updateVibrateInfo(boolean isOn){
		if(isOn){
			pref.edit().putBoolean("Vibrate", true).commit();
			SetupActivity.this.findViewById(R.id.textView_setting_vib_desc).setAlpha(1.0f);
		}else{
			pref.edit().putBoolean("Vibrate", false).commit();
			SetupActivity.this.findViewById(R.id.textView_setting_vib_desc).setAlpha(0.5f);
		}
	}

	/*
	 * Update TextView which shows alarm time.
	 */
	private void updateDisplay(int id) {
		int hour, minute;
		String ampm, whichOne;
		TextView targetTextView;
		LinearLayout alarm_wrap;
		boolean onoff;
		
		if(id == 1){
			hour = mHour;
			minute = mMinute;
			targetTextView = one_alarm_time;
			alarm_wrap = one_alarm_wrap;
			onoff = one_onoff;
			whichOne = "one";
		}else if(id == 2){
			hour = mHourTwo;
			minute = mMinuteTwo;
			targetTextView = two_alarm_time;
			alarm_wrap = two_alarm_wrap;
			onoff = two_onoff;
			whichOne = "two";
		}else
			return;
		
		int org_hour = hour;
		if (hour < 12) {
			ampm = "오전";
		} else {
			ampm = "오후";
			hour = hour - 12;
			
			if(hour == 0)
				hour = 12;
		}		
		targetTextView.setText(new StringBuilder().append(ampm + " ")
				.append(String.format("%02d", hour)).append(":").append(String.format("%02d", minute)));
		
		alarm_wrap.setClickable(onoff);
		alarm_wrap.setAlpha(onoff ? 1.0f : 0.5f);
		
		String set = onoff ? "set" : "noset";
		pref = getSharedPreferences("Setting", 0);
		pref.edit()
			.putString(whichOne + "_alarm", set)
			.putInt(whichOne + "_alarm_hour", org_hour)
			.putInt(whichOne + "_alarm_minute", minute)
			.commit();
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
	
	/*
	 * @param type : 0 for new Alarm, 1 for Alarm time change
	 **/
	public static void UpdateAlarm(Context context, int type){
		boolean isOneSet, isTwoSet;
		int one_hour, two_hour, one_minute, two_minute;
		
		// Get data from SharedPref.
		SharedPreferences pref = context.getSharedPreferences("Setting", 0);
		isOneSet		= pref.getString("one_alarm", "set").equals("set");
		one_hour		= pref.getInt("one_alarm_hour", 7);
		one_minute	= pref.getInt("one_alarm_minute", 0);
		
		isTwoSet		= pref.getString("two_alarm", "set").equals("set");
		two_hour		= pref.getInt("two_alarm_hour", 23);
		two_minute	= pref.getInt("two_alarm_minute", 0);

		// Set Intent and Alarm
		Intent intent = new Intent(context.getApplicationContext(), NotifyService.class); 
		PendingIntent pendingI, pendingI_two;
		int flag = PendingIntent.FLAG_UPDATE_CURRENT;
		if(type == 1)
			flag = PendingIntent.FLAG_NO_CREATE;
		pendingI	 = PendingIntent.getService(context.getApplicationContext(), 0, intent, flag);
		pendingI_two = PendingIntent.getService(context.getApplicationContext(), 1, intent, flag);
		
		long triggerTime ;
		PendingIntent pi_cancel = PendingIntent.getService(context.getApplicationContext(), 0, intent, PendingIntent.FLAG_NO_CREATE);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		if(pi_cancel != null)
			alarmManager.cancel(pi_cancel);
		if(isOneSet){
			triggerTime = getTriggerTime(one_hour, one_minute);
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime, oneDay, pendingI);
			Calendar cal = new GregorianCalendar();
			cal.setTimeInMillis(triggerTime);
			Log.d("VOM SetupActivity", "Set Alarm1 : " + String.format("%02d/%02d %02d:%02d", cal.get(GregorianCalendar.MONTH) +1, cal.get(GregorianCalendar.DAY_OF_MONTH), one_hour, one_minute));
		}
		
		pi_cancel = PendingIntent.getService(context.getApplicationContext(), 1, intent, PendingIntent.FLAG_NO_CREATE);
		if(pi_cancel != null)
			alarmManager.cancel(pi_cancel);
		if(isTwoSet){
			triggerTime = getTriggerTime(two_hour, two_minute);
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime, oneDay, pendingI_two);
			Calendar cal = new GregorianCalendar();
			cal.setTimeInMillis(triggerTime);
			Log.d("VOM SetupActivity", "Set Alarm2 : " + String.format("%02d/%02d %02d:%02d", cal.get(GregorianCalendar.MONTH) +1, cal.get(GregorianCalendar.DAY_OF_MONTH), two_hour, two_minute));
		}
	}
	
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			updateDisplay(1);
			
			Log.d("VOM", "Time Set(1)");
			
			UpdateAlarm(SetupActivity.this, 0);
		}
	};
	
	private TimePickerDialog.OnTimeSetListener mTimeSetListenerTwo = new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHourTwo = hourOfDay;
			mMinuteTwo = minute;
			updateDisplay(2);
			
			Log.d("VOM", "Time Set(2)");
			
			UpdateAlarm(SetupActivity.this, 0);
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
