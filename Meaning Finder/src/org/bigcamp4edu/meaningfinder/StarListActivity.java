package org.bigcamp4edu.meaningfinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.bigcamp4edu.meaningfinder.util.StarListItemType;

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

		requestWindowFeature(Window.FEATURE_NO_TITLE); // �׼ǹ� ����
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN); // ���¹� ����
		
		setContentView(R.layout.activity_star_list);
		
		
		// '����' ��ư ��� ����
        ((Button) findViewById(R.id.btn_starlist_setting)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(StarListActivity.this, SetupActivity.class);
	            startActivity(intent);
			}
		});
        
        // ListActivity ��ư ��� ����
        ((Button) findViewById(R.id.btn_starlist_tolist)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(StarListActivity.this, ListActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	            startActivity(intent);
	            
	            finish();
			}
		});
        
	}
	
	class StarListArrayAdapter extends BaseAdapter {
		
		StarImageMapper starImageSetter = new StarImageMapper(StarListActivity.this);
		ArrayList<StarListItemType> list = new ArrayList<StarListItemType>(Var.list_stars.values());

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public StarListItemType getItem(int position) {
			return list.get(position);
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
        	
        	final String star_image_name = list.get(position).starImgName;
        	starImageSetter.setStarImageName(star_image_name);
        	listStar.setImageDrawable(starImageSetter.getStarImage());
        	
        	final int f_position = position;
        	listStar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO: Ư�� ���ڸ��� ������ ���� �������� ��ȯ
//					Toast.makeText(StarListActivity.this, star_image_name, Toast.LENGTH_SHORT).show();
					
					Intent intent = new Intent(StarListActivity.this, StarActivity.class);
					intent.putExtra("StarName", list.get(f_position).starName);
//					intent.putExtra("StarNameEn", Var.list_stars.get(f_position).starName);
					intent.putExtra("StarImg", list.get(f_position).starImgName);
					startActivity(intent);
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
