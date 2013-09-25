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

public class StarListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE); // �׼ǹ� ����
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN); // ���¹� ����
		
		setContentView(R.layout.activity_star_list);
		
		
		// '����' ��ư ��� ����
        Button setting_btn	= (Button) findViewById(R.id.btn_starlist_setting);
        setting_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(StarListActivity.this, SetupActivity.class);
	            startActivity(intent);
			}
		});
        
        // ���ڸ� ��ư ��� ����
        ((Button) findViewById(R.id.btn_starlist_tolist)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(StarListActivity.this, ListActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	            startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.star_list, menu);
		return true;
	}

}
