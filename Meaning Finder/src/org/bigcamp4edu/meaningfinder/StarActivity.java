package org.bigcamp4edu.meaningfinder;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StarActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 액션바 제거
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN); // 상태바 제거
        setContentView(R.layout.activity_star);
        
        // '설정' 버튼 기능 구현
        ((Button) findViewById(R.id.btn_star_setting)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(StarActivity.this, SetupActivity.class);
	            startActivity(intent);
			}
		});
        
        // ListActivity 버튼 기능 구현
        ((Button) findViewById(R.id.btn_star_tolist)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(StarActivity.this, ListActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	            startActivity(intent);
	            
	            finish();
			}
		});
    
        Intent fromIntent = getIntent();
        String starName = fromIntent.getExtras().getString("StarName");
        String starNameEn = fromIntent.getExtras().getString("StarNameEn");
        String starImg = fromIntent.getExtras().getString("StarImg");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.star, menu);
        return true;
    }
    
}
