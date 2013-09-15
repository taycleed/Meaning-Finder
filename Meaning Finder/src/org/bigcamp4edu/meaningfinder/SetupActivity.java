package org.bigcamp4edu.meaningfinder;

import java.util.Calendar;

import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

public class SetupActivity extends Activity {

	private TextView one_alarm_time;
	private LinearLayout one_alarm_wrap;
	private int mHour;
	private int mMinute;
	static final int TIME_DIALOG_ID = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE); // 액션바 제거
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN); // 상태바 제거

		setContentView(R.layout.activity_setup);

		// capture our View elements
		one_alarm_time = (TextView) findViewById(R.id.one_alarm_time);
		one_alarm_wrap = (LinearLayout) findViewById(R.id.one_alarm_wrap);

		one_alarm_wrap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(TIME_DIALOG_ID);
			}
		});
		
		final Calendar c	= Calendar.getInstance();
		mHour				= c.get(Calendar.HOUR_OF_DAY);
		mMinute				= c.get(Calendar.MINUTE);
		
		updateDisplay();

	}
	
	private void updateDisplay(){
		one_alarm_time.setText(
				new StringBuilder().append(pad(mHour)).append(":").append(pad(mMinute))
				);
	}
	
	private static String pad(int c)
	{		
		if(c >= 10){
			return String.valueOf(c);
		}else{
			return "0"+String.valueOf(c);
		}
	}
	
	TimePickerDialog.OnTimeSetListener mTimeSetListener = 
			new TimePickerDialog.OnTimeSetListener() {
				
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					// TODO Auto-generated method stub
					mHour	= hourOfDay;
					mMinute	= minute;
					updateDisplay();
				}
			};

	
	protected Dialog onCreaDialog(int id){
		switch(id){
			case TIME_DIALOG_ID:
				return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, false);
		}
		
		return null;
	}
		
	//
	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.setup, menu);
	// return true;
	// }

}
