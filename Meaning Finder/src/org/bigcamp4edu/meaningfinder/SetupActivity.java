package org.bigcamp4edu.meaningfinder;

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

// TODO: 알림 On/Off 기능 추가 구현
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
		
		if(pendingI == null || pendingI_two == null){
			setPendingIntent();
		}

		setContentView(R.layout.activity_setup);

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

		pref 					= getSharedPreferences("Setting", 0);
		String one_alarm		= pref.getString("one_alarm", "noset");
		int one_alarm_hour		= pref.getInt("one_alarm_hour", 7);
		int one_alarm_minute	= pref.getInt("one_alarm_minute", 0);
		
		String two_alarm		= pref.getString("two_alarm", "noset");
		int two_alarm_hour		= pref.getInt("two_alarm_hour", 23);
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
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);	// 현재 열린 모든 Activity 닫기
                startActivity(intent);
				finish();	// Setup 액티비티 닫기
			}
		});
		
		((CheckBox) findViewById(R.id.checkBox_setting_time1)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO 
				if(isChecked){

				}else{
					
				}
			}
		});
		((CheckBox) findViewById(R.id.checkBox_setting_time2)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO 
				if(isChecked){

				}else{
					
				}
			}
		});
		
	}

	/*
	 * Update TextView which shows alarm time.
	 */
	private void updateDisplay(int id) {
		int hour, minute;
		String ampm, whichOne;
		TextView targetTextView;
		if(id == 1){
			hour = mHour;
			minute = mMinute;
			targetTextView = one_alarm_time;
			whichOne = "one";
		}else if(id == 2){
			hour = mHourTwo;
			minute = mMinuteTwo;
			targetTextView = two_alarm_time;
			whichOne = "two";
		}else
			return;
		
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
		
		pref = getSharedPreferences("Setting", 0);
		pref.edit()
			.putString(whichOne + "_alarm", "set")
			.putInt(whichOne + "_alarm_hour", hour)
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
	
	public static void UpdateAlarm(Context context){
		boolean isOneSet, isTwoSet;
		int one_hour, two_hour, one_minute, two_minute;
		
		// Get data from SharedPref.
		
		// Parse time
		
		// Set Intent and Alarm
		
		Intent intent = new Intent(context, NotifyService.class); 
		PendingIntent pendingI,pendingI_two;
		pendingI	 = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		pendingI_two = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingI);
//		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, getTriggerTime(mHour, mMinute), oneDay, pendingI);
	}
	
	private void setPendingIntent(){
		//사용자가 알람을 확인하고 클릭했을때 새로운 액티비티를 시작할 인텐트 객체
		Intent intent = new Intent(SetupActivity.this, NotifyService.class);
		Intent intent2 = new Intent(SetupActivity.this, NotifyService.class);
		
		//인텐트 객체를 포장해서 전달할 인텐트 전달자 객체
		pendingI	 = PendingIntent.getService(SetupActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		pendingI_two = PendingIntent.getService(SetupActivity.this, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			updateDisplay(1);
			
			Log.d("VOM", "Time Set(1)");
			
//			UpdateAlarm(SetupActivity.this);
			
			AlarmManager alarmManager = (AlarmManager) SetupActivity.this.getSystemService(Context.ALARM_SERVICE);
			alarmManager.cancel(pendingI);
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, getTriggerTime(mHour, mMinute), oneDay, pendingI);
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
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, getTriggerTime(mHourTwo, mMinuteTwo), oneDay, pendingI_two);
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
