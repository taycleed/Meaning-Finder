package org.bigcamp4edu.meaningfinder;

import java.util.ArrayList;
import org.bigcamp4edu.meaningfinder.util.QuestionListItemType;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class StarActivity extends Activity {
	
	String starName ="";
	ArrayList<QuestionListItemType> list;

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
        
        // StarActivity 버튼 기능 구현
        ((Button) findViewById(R.id.btn_star_tostarlist)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(StarActivity.this, StarListActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	            startActivity(intent);
	            
	            finish();
			}
		});
        
        // 질문 Show/Unshow 버튼 기능 구현
        ((Button) findViewById(R.id.btn_star_questiononoff)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				View listView = StarActivity.this.findViewById(R.id.listView_star_questions);
				if(listView.getVisibility() == View.VISIBLE){
					listView.setVisibility(View.GONE);
				}else{
					listView.setVisibility(View.VISIBLE);
				}
			}
		});
        findViewById(R.id.listView_star_questions).setVisibility(View.GONE);
    
        Intent fromIntent = getIntent();
        starName = fromIntent.getExtras().getString("StarName");
//        String starNameEn = fromIntent.getExtras().getString("StarNameEn");
        String starImg = fromIntent.getExtras().getString("StarImg");
        
        ((TextView) findViewById(R.id.textview_star_title)).setText(starName);
        StarImageMapper siMapper = new StarImageMapper(this);
        siMapper.setStarImageName(starImg);
        ((ImageView) findViewById(R.id.imageView_star)).setImageDrawable(siMapper.getStarImageBig());
        siMapper = null;
    }
    
    OnItemClickListener onListItemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent(StarActivity.this, QuestionViewActivity.class);
        	intent.putExtra("userId", Var.userId);
        	intent.putExtra("questionNo", Integer.toString(list.get(position).listReqNo));
            startActivity(intent);
		}
	};
	
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	
    	list = new ArrayList<QuestionListItemType>();
		for(QuestionListItemType item : Var.list_questions){
			if(item.listStarName.equals(starName))
				list.add(item);
		}
    	
    	ListView listview = (ListView) findViewById(R.id.listView_star_questions);
    	listview.setAdapter(new StarListArrayAdapter());
    	listview.setOnItemClickListener(onListItemClick);
    }
    
    public class StarListArrayAdapter extends BaseAdapter {
//    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss  ");
    	
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
        	viewHolder holder;
        	 
        	if(convertView == null){
        		LayoutInflater inflater = getLayoutInflater();
        		convertView = inflater.inflate(R.layout.starquestionlist_item, parent, false);
        		holder 	= new viewHolder();
        		holder.listLinear	= (LinearLayout) convertView.findViewById(R.id.listItemLayout_starquestion);
        		holder.listQuestion	= (TextView) convertView.findViewById(R.id.textView_starquestion_item);
        		
        		convertView.setTag(holder);
        	}
        	else{
        		holder	= (viewHolder) convertView.getTag();
        	}
        	
        	holder.listQuestion.setText(list.get(position).listText);
//        	GregorianCalendar calendar = new GregorianCalendar();
//        	calendar.setTimeInMillis(Var.list_questions.get(position).timeStamp * 1000);
//        	holder.listDate.setText( dateFormat.format(calendar.getTime()) );
        	
//        	Log.d("VOM List Adapter", dateFormat.format(calendar.getTime()));
        	
            return convertView;
        }
        
        public class viewHolder
        {
          TextView listQuestion;
          LinearLayout listLinear;
        }

		@Override
		public Object getItem(int arg0) {
			return list.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return list.get(arg0).listReqNo;
		}
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.star, menu);
        return true;
    }
    
}
