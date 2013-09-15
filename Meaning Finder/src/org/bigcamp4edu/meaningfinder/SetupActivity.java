package org.bigcamp4edu.meaningfinder;

import java.util.Calendar;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SetupActivity extends Activity {
	
	private TextView one_alarm_time;
	private LinearLayout one_alarm_wrap;
	private int mHour;
	private int mMinute;
	static final int TIME_DIALOG_ID = 0;

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);								// 액션바 제거
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);						// 상태바 제거
        
        setContentView(R.layout.activity_setup);
        
        // capture our View elements
        one_alarm_time = (TextView) findViewById(R.id.one_alarm_time);
        one_alarm_wrap = (LinearLayout) findViewById(R.id.one_alarm_wrap);

        // add a click listener to the button
        one_alarm_wrap.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(TIME_DIALOG_ID);
			}
        });

        // get the current time

        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        
        // display the current date
        updateDisplay();

        
    }
    
 // updates the time we display in the TextView

    private void updateDisplay() {

    	one_alarm_time.setText(
                      new StringBuilder()
                                       .append(pad(mHour)).append(":")
                                       .append(pad(mMinute)));
    }

     

    private static String pad(int c) {
        if (c >= 10){
        	return String.valueOf(c);
        }
        else{
        	return "0" + String.valueOf(c);	
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setup, menu);
        return true;
    }
    
}
