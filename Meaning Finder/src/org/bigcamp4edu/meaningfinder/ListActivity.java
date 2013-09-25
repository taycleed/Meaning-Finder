package org.bigcamp4edu.meaningfinder;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/*
 * ListActivity shows completed questions.
 */
public class ListActivity extends Activity {
	ListView listView;					// 리스트 뷰 xml
	VOMArrayAdapter adt;				// 리스트 어뎁터
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);								// 액션바 제거
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);						// 상태바 제거
        
        setContentView(R.layout.activity_list);
        listView = (ListView)findViewById(R.id.listView_questions);
        
        // '설정' 버튼 기능 구현
        Button setting_btn	= (Button) findViewById(R.id.setting_btn);
        setting_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ListActivity.this, SetupActivity.class);
	            startActivity(intent);
			}
		});
        
        // 별자리 버튼 기능 구현
        ((Button) findViewById(R.id.star_list)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ListActivity.this, StarListActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	            startActivity(intent);
			}
		});
        
        // '질문/답변' 버튼 기능 구현
        Button btn_gotoquestion	= (Button) findViewById(R.id.btn_gotoquestion);
        btn_gotoquestion.setOnClickListener(new OnClickListener() {

        	@Override
			public void onClick(View v) {
				Intent intent = new Intent(ListActivity.this, QuestionActivity.class);
	            startActivity(intent);
			}
		});
        
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	
    	Thread dataParsingThread = new Thread(new Runnable() {
			@Override
			public void run() {
//				if(Var.listText.size() == 0){
		        	XmlParser.getListText();
//		        }
			}
		});
        dataParsingThread.start();
        try {
			dataParsingThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        adt = new VOMArrayAdapter();
        listView.setAdapter(adt);
        
        listView.setOnItemClickListener( new ListViewItemClickListener() );
        
        Button btn_gotoquestion	= (Button) findViewById(R.id.btn_gotoquestion);
        
        if(NotifyService.HasSavedToday(this)){	// 오늘 질문에 답한 기록이 있다면
        	btn_gotoquestion.setVisibility(View.GONE);
        }else{
        	btn_gotoquestion.setVisibility(View.VISIBLE);
        }
	}
    
    
    private class ListViewItemClickListener implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
        {
//            AlertDialog.Builder alertDlg = new AlertDialog.Builder(view.getContext());
//            alertDlg.setPositiveButton( "asdasd", new DialogInterface.OnClickListener()
//            {
//                 @Override
//                 public void onClick( DialogInterface dialog, int which ) {
//                     dialog.dismiss();  // AlertDialog를 닫는다.
//                 }
//            });
//            
//            alertDlg.setMessage( Var.listReqNo.get(position) );
//            alertDlg.show();
        		Intent intent = new Intent(ListActivity.this, QuestionViewActivity.class);
            	intent.putExtra("userId", Var.userId);
            	intent.putExtra("questionNo", Integer.toString(Var.list_questions.get(position).listReqNo));
                startActivity(intent);
        }        
    }

    




	setStarImage setStar = new setStarImage(this);
    
    
    Drawable starDrawable;
    public class VOMArrayAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return Var.list_questions.size();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
        	viewHolder holder;
        	 
        	if(convertView == null){
        		LayoutInflater inflater = getLayoutInflater();
        		convertView = inflater.inflate(R.layout.list_item, parent, false);
        		holder 	= new viewHolder();
        		holder.listLinear	= (LinearLayout) convertView.findViewById(R.id.listItemLayout);
        		holder.listQuest	= (TextView) convertView.findViewById(R.id.textViewItem);
        		holder.listStar		= (ImageView) convertView.findViewById(R.id.imageViewItem);
        		
        		convertView.setTag(holder);
        	}
        	else{
        		holder	= (viewHolder) convertView.getTag();
        	}
        	
        	String	star_image_name;
        	star_image_name	= Var.list_questions.get(position).listImgName;
        	
    		setStar.setStarImageName(star_image_name);
    		starDrawable	= setStar.getStarImage();

        	holder.listQuest.setText(Var.list_questions.get(position).listText);
        	holder.listStar.setImageDrawable(starDrawable);
        	
        	
        	
            return convertView;
        }
        
        public class viewHolder
        {
          LinearLayout listLinear;
          TextView listQuest;
          ImageView listStar;
        }

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}
    }
   
    
    
    
}
