package org.bigcamp4edu.meaningfinder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.bigcamp4edu.meaningfinder.QuestionActivity.QuestionGetterAsync;
import org.bigcamp4edu.meaningfinder.util.QuestionListItemType;

import com.trevorpage.tpsvg.SVGParserRenderer;
import com.trevorpage.tpsvg.SVGView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class StarActivity extends Activity {
	
	String starName ="", starImg="";
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
        
        // 'To Star List' 버튼 기능 구현
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
        findViewById(R.id.listView_star_questions).setVisibility(View.GONE);	// 일단 질문들은 안보이게 해놓음.
    
        Intent fromIntent = getIntent();
        starName = fromIntent.getExtras().getString("StarName");
//        String starNameEn = fromIntent.getExtras().getString("StarNameEn");
        starImg = fromIntent.getExtras().getString("StarImg");
        
        ((TextView) findViewById(R.id.textview_star_title)).setText(starName);	// Title: Constellation name
        // Prepare question items
    	list = new ArrayList<QuestionListItemType>();
//		for(QuestionListItemType item : Var.list_questions){
//			if(item.listStarName.equals(starName))
//				list.add(item);
//		}
        
//		// Set Contellation Image
//		SetStarImage(starImg);
		
		ListView listview = (ListView) findViewById(R.id.listView_star_questions);
    	listview.setAdapter(new StarListArrayAdapter());
    	listview.setOnItemClickListener(new OnItemClickListener() {
    		@Override
    		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    			Intent intent = new Intent(StarActivity.this, QuestionViewActivity.class);
            	intent.putExtra("userId", Var.userId);
            	intent.putExtra("questionNo", Integer.toString(list.get(position).listReqNo));
                startActivity(intent);
    		}
    	});
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	
    	(new StarQuestionGetterAsync()).execute();
    }
    
    class StarQuestionGetterAsync extends AsyncTask<Void, Void, Void> {
    	@Override
    	protected void onPreExecute() {
    		list.clear();
    	};
    	
		@Override
		protected Void doInBackground(Void... params) {
//			XmlParser.getStarQuestionListText(starName, list);
			XmlParser.getListText();
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			for (QuestionListItemType item : Var.list_questions) {
				if (item.listStarName.equals(starName))
					list.add(item);
			}
			
			// Set Constellation Image
			SetStarImage(starImg);
		}
    }
    
    private FrameLayout SetStarImage(String starImg){
		StarImageMapper siMapper = new StarImageMapper(this);
	    siMapper.setStarImageName(starImg);
	    FrameLayout fl = (FrameLayout) findViewById(R.id.frameLayout_star_image);
		SVGParserRenderer svgRenderer = new SVGParserRenderer(this, siMapper.getStarImageSvgId());
		int star_count = list.size();
		
		// 
		SVGView svgView_dimmed = new SVGView(this);
		svgView_dimmed.setSVGRenderer(svgRenderer, "line_dim");
		svgView_dimmed.setBackgroundColor(0x00999999);
		fl.addView(svgView_dimmed);
		
		for(int i = 1 ; i <= star_count ; i++){
			if(i == star_count){
				SVGView svgView ;
				if(i != 1){				
					svgView = new SVGView(this);
					svgView.setSVGRenderer(svgRenderer, String.format("line_%02d", i));
					svgView.setBackgroundColor(0x00999999);
					fl.addView(svgView);
				}
				
				// TODO: add animation on star
				svgView = new SVGView(this);
				svgView.setSVGRenderer(svgRenderer, String.format("star_%02d", i));
				svgView.setBackgroundColor(0x00999999);
				fl.addView(svgView);
				svgView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.alpha_1_0_900msec));
			}else{
				SVGView svgView = new SVGView(this);
				svgView.setSVGRenderer(svgRenderer, String.format("num_%02d", i));
				svgView.setBackgroundColor(0x00999999);
				
				fl.addView(svgView);
			}
		}       
	    
	    siMapper = null;
	    
	    return fl;
	}

	public class StarListArrayAdapter extends BaseAdapter {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
    	
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
        	viewHolder holder;
        	 
        	if(convertView == null){
        		LayoutInflater inflater = getLayoutInflater();
        		convertView = inflater.inflate(R.layout.list_item, parent, false);
        		holder 	= new viewHolder();
        		holder.listLinear	= (LinearLayout) convertView.findViewById(R.id.listItemLayout);
        		holder.listQuestion	= (TextView) convertView.findViewById(R.id.textView_list_item);
        		holder.listDate = (TextView) convertView.findViewById(R.id.textView_listItem_date);
        		holder.listStar = (ImageView) convertView.findViewById(R.id.imageView_list_item);
        		convertView.setTag(holder);
        	}
        	else{
        		holder	= (viewHolder) convertView.getTag();
        	}
        	
        	GregorianCalendar calendar = new GregorianCalendar();
        	calendar.setTimeInMillis(Var.list_questions.get(position).timeStamp * 1000);
        	holder.listDate.setText( dateFormat.format(calendar.getTime()) );
        	holder.listStar.setVisibility(View.GONE);	// 이 액티비티에서는 보여줄 필요없음.
        	holder.listQuestion.setText(list.get(position).listText);
        	
        	Log.d("VOM List Adapter", dateFormat.format(calendar.getTime()));
        	
            return convertView;
        }
        
        public class viewHolder
        {
          TextView listQuestion;
          LinearLayout listLinear;
          ImageView listStar;
          TextView listDate;
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
}
