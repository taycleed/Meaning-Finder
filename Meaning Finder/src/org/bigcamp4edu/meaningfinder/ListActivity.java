package org.bigcamp4edu.meaningfinder;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/*
 * ListActivity shows completed questions.
 */
public class ListActivity extends Activity {
	ListActivity	ListActivity;
	ListView listView;					// 리스트 뷰 xml
	VOMArrayAdapter adt;				// 리스트 어뎁터
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);								// 액션바 제거
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);						// 상태바 제거
        
        setContentView(R.layout.activity_list);
        listView = (ListView)findViewById(R.id.listView_questions);
        
        Button setting_btn	= (Button) findViewById(R.id.setting_btn);
        setting_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ListActivity.this, SetupActivity.class);
	            startActivity(intent);
			}
		});
        
       
        
        Thread dataParsingThread = new Thread(new Runnable() {
			@Override
			public void run() {
				if(Var.listText.size() == 0){
		        	XmlParser.getListText();
		        }
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

    }
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	
    	 Thread dataParsingThread = new Thread(new Runnable() {
 			@Override
 			public void run() {
 				if(Var.listText.size() == 0){
 		        	XmlParser.getListText();
 		        }
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
    };
    
    
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
            	intent.putExtra("questionNo", Var.listReqNo.get(position));
                startActivity(intent);
        }        
    }

    




	setStarImage setStar = new setStarImage(this);
    
    
    Drawable starDrawable;
    public class VOMArrayAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return Var.listText.size();
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
        	star_image_name	= (String) Var.listImgName.get(position);
        	
    		setStar.setStarImage(star_image_name);
    		starDrawable	= setStar.getStarImage();

        	holder.listQuest.setText(Var.listText.get(position));
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
			// TODO Auto-generated method stub
			return arg0;
		}



		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}
    }
   
    
    
    
}
