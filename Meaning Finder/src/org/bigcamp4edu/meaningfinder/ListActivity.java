package org.bigcamp4edu.meaningfinder;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
        
        Thread dataParsingThread = new Thread(new Runnable() {
			@Override
			public void run() {
				if(Var.listText.size() == 0){
					Log.i("TEXTDATA", "parsing...");
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
    }


    
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

        	holder.listQuest.setText(Var.listText.get(position));
        	Image_Downloader.download(Var.listImgUrl.get(position), holder.listStar);
        	
//            LayoutInflater inflater = getLayoutInflater();
//            View row;
//            row = inflater.inflate(R.layout.list_item, parent, false);
//            LinearLayout listItemLayout;
//            TextView question;
//            ImageView star;
//            listItemLayout = (LinearLayout) row.findViewById(R.id.listItemLayout);
//            question = (TextView) row.findViewById(R.id.textViewItem);
//            star = (ImageView) row.findViewById(R.id.imageViewItem);
//            question.setText(Var.listText.get(position));
//            Image_Downloader.download(Var.listImgUrl.get(position), star);
//            listItemLayout.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					//Intent intent = new Intent();
//					//intent.putExtra(name, value);
//					Toast.makeText(getBaseContext(), position+"번째 아이템", Toast.LENGTH_LONG).show();
//				}
//			});
//            list.get(position).star
//            star.setImageResource();

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
