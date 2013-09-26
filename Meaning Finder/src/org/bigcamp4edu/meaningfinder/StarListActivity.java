package org.bigcamp4edu.meaningfinder;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class StarListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE); // 액션바 제거
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN); // 상태바 제거
		
		setContentView(R.layout.activity_star_list);
		
		
		// '설정' 버튼 기능 구현
        ((Button) findViewById(R.id.btn_starlist_setting)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(StarListActivity.this, SetupActivity.class);
	            startActivity(intent);
			}
		});
        
        // 별자리 버튼 기능 구현
        ((Button) findViewById(R.id.btn_starlist_tolist)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(StarListActivity.this, ListActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	            startActivity(intent);
			}
		});
        
	}
	
	class StarListArrayAdapter extends BaseAdapter {
		
		setStarImage starImageSetter = new setStarImage(StarListActivity.this);

		@Override
		public int getCount() {
			return Var.list_stars.size();
		}

		@Override
		public String getItem(int position) {
			return Var.list_stars.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
    		LayoutInflater inflater = getLayoutInflater();
    		convertView = inflater.inflate(R.layout.starlist_item, parent, false);
    		ImageView listStar		= (ImageView) convertView.findViewById(R.id.imageView_stars);
        	
        	final String star_image_name = Var.list_stars.get(position);
        	starImageSetter.setStarImageName(star_image_name);
        	listStar.setImageDrawable(starImageSetter.getStarImage());
        	
        	listStar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(StarListActivity.this, star_image_name, Toast.LENGTH_SHORT).show();
				}
			});
        	
            return convertView;
		}
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		GridView gridView = (GridView) findViewById(R.id.gridview_starlist_stars);
		gridView.setAdapter(new StarListArrayAdapter());
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.star_list, menu);
		return true;
	}

}
