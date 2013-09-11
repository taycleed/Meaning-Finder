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
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
        
        listView.setOnItemClickListener( new ListViewItemClickListener() );

    }
    
    private class ListViewItemClickListener implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
        {
            AlertDialog.Builder alertDlg = new AlertDialog.Builder(view.getContext());
            alertDlg.setPositiveButton( "asdasd", new DialogInterface.OnClickListener()
            {
                 @Override
                 public void onClick( DialogInterface dialog, int which ) {
                     dialog.dismiss();  // AlertDialog를 닫는다.
                 }
            });
            
            alertDlg.setMessage( Var.listReqNo.get(position) );
            alertDlg.show();
        }        
    }

    




    
    
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
        	
        	if(star_image_name.equals("con_aries_small.png")){
        		starDrawable	= (Drawable) getResources().getDrawable(R.drawable.con_aries_small);
        	}else if(star_image_name.equals("con_camelopardalis_small.png")){
        		starDrawable	= (Drawable) getResources().getDrawable(R.drawable.con_camelopardalis_small);
        	}else if(star_image_name.equals("con_cancerconstellation_small.png")){
        		starDrawable	= (Drawable) getResources().getDrawable(R.drawable.con_cancerconstellation_small);
        	}else if(star_image_name.equals("con_canisminoris_small.png")){
        		starDrawable	= (Drawable) getResources().getDrawable(R.drawable.con_canisminoris_small);
        	}else if(star_image_name.equals("con_capricornus_small.png")){
        		starDrawable	= (Drawable) getResources().getDrawable(R.drawable.con_capricornus_small);
        	}else if(star_image_name.equals("con_casiopea_small.png")){
        		starDrawable	= (Drawable) getResources().getDrawable(R.drawable.con_casiopea_small);
        	}else if(star_image_name.equals("con_comaberenies_small.png")){
        		starDrawable	= (Drawable) getResources().getDrawable(R.drawable.con_comaberenies_small);
        	}else if(star_image_name.equals("con_gemin_small.png")){
        		starDrawable	= (Drawable) getResources().getDrawable(R.drawable.con_gemin_small);
        	}else if(star_image_name.equals("con_leo_small.png")){
        		starDrawable	= (Drawable) getResources().getDrawable(R.drawable.con_leo_small);
        	}else if(star_image_name.equals("con_sagittarius_small.png")){
        		starDrawable	= (Drawable) getResources().getDrawable(R.drawable.con_sagittarius_small);
        	}else if(star_image_name.equals("con_scorpius_small.png")){
        		starDrawable	= (Drawable) getResources().getDrawable(R.drawable.con_scorpius_small);
        	}else if(star_image_name.equals("con_ursamajor_small.png")){
        		starDrawable	= (Drawable) getResources().getDrawable(R.drawable.con_ursamajor_small);
        	}
        	
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
