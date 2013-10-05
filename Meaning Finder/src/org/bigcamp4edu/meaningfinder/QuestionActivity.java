package org.bigcamp4edu.meaningfinder;

import java.util.GregorianCalendar;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class QuestionActivity extends Activity {

	private TextView star_name; // 별자리 한글이름
	private TextView star_name_en; // 별자리 영어이름
	private Drawable starDrawable;

	private ImageView question_star_image; // 질문에 해당하는 별자리 image view
	private TextView question_title; // 질문
	
	private EditText get_answer_text;
	
	private Button button_cancle;
	private Button button_join_submit;
	
	public class QuestionGetterAsync extends AsyncTask<Void, Void, Void>{
		
		@Override
		protected void onPreExecute() {
			Var.InitLoginInfo(QuestionActivity.this);
			
			if(!Var.LOGIN_STATE){
				// Login이 필요함
				Intent intent = new Intent(QuestionActivity.this, LogoActivity.class);
				intent.putExtra("fromQuestion", true);
				startActivity(intent);
				
				QuestionActivity.this.finish();
				this.cancel(true);
			}
		}
			
		@Override
		protected Void doInBackground(Void... params) {
			XmlParser.getQuestion();
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			star_name.setText(Var.get_star_name);
			star_name_en.setText(Var.get_star_name_en);
			
			StarImageMapper setStar = new StarImageMapper(QuestionActivity.this);
			setStar.setStarImageName(Var.get_star_img);
			starDrawable	= setStar.getStarImage();
			question_star_image.setImageDrawable(starDrawable);
			question_star_image.setVisibility(View.VISIBLE);
			
			question_title.setText(Var.get_question_name);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);

		star_name 			= (TextView) findViewById(R.id.get_star_name);
		star_name_en 		= (TextView) findViewById(R.id.get_star_name_en);

		question_star_image = (ImageView) findViewById(R.id.question_get_star_image);
		question_title 		= (TextView) findViewById(R.id.question_get_title);
		
		button_cancle		= (Button) findViewById(R.id.button_cancle);
		button_join_submit	= (Button) findViewById(R.id.button_join_submit);
		
		get_answer_text		= (EditText) findViewById(R.id.get_answer_text);
		
		
		// '취소' 버튼 구현
		button_cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(QuestionActivity.this, ListActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	            startActivity(intent);
	            
	            finish();
			}
		});

		// '저장' 버튼 구현
		button_join_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Var.get_answer	= get_answer_text.getText().toString();
				
				Thread x = new Thread(new Runnable() {

					@Override
					public void run() {
						if(XmlParser.insertAnswer())
						{
							long dateTimeInMilli = GregorianCalendar.getInstance().getTimeInMillis(); 
							getSharedPreferences("Setting", 0).edit().putLong(DB.LAST_ANSWER_DATE, dateTimeInMilli).commit();
							Log.d("VOM QuestionActivity", "Save time in milli : " + Long.toString(dateTimeInMilli));
							
							Intent intent = new Intent(QuestionActivity.this, StarActivity.class);
							intent.putExtra("StarName", star_name.getText());
							intent.putExtra("StarNameEn", star_name_en.getText());
							intent.putExtra("StarImg", Var.get_star_img);
							startActivity(intent);
			            	
			            	finish();
						}
					}
				});
				x.start();
				
				try {
					x.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		});
		
	}

	@Override
	protected void onResume() {
		super.onResume();

		// 질문을 받아옴.
		(new QuestionGetterAsync()).execute();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.question, menu);
		return true;
	}

}
