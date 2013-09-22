package org.bigcamp4edu.meaningfinder;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class QuestionActivity extends Activity {

	private Intent intent; // intent
	private String userId; // userId
	private String questionNo; // ������ȣ

	private TextView star_name; // ���ڸ� �ѱ��̸�
	private TextView star_name_en; // ���ڸ� �����̸�
	private Drawable starDrawable;

	private ImageView question_star_image; // ������ �ش��ϴ� ���ڸ� image view
	private TextView question_title; // ����
	private TextView answer_text; // �亯
	
	private EditText get_answer_text;
	
	private Button button_cancle;
	private Button button_join_submit;
	
	public class QuestionGetterAsync extends AsyncTask<Void, Void, Void>{
		
		@Override
		protected void onPreExecute() {
			Var.InitLoginInfo(QuestionActivity.this);
			
			if(!Var.LOGIN_STATE){
				// TODO: Login�� �ʿ���
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
			
			setStarImage setStar = new setStarImage(QuestionActivity.this);
			setStar.setStarImage(Var.get_star_img);
			starDrawable	= setStar.getStarImage();
			question_star_image.setImageDrawable(starDrawable);
			
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
		
		// ������ �޾ƿ�.
		(new QuestionGetterAsync()).execute();
		
		// '���' ��ư ����
		button_cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(QuestionActivity.this, ListActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	            startActivity(intent);
	            
	            finish();
			}
		});

		// '����' ��ư ����
		button_join_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Var.get_answer	= get_answer_text.getText().toString();
				
				Thread x = new Thread(new Runnable() {

					@Override
					public void run() {
						if(XmlParser.insertAnswer())
						{
							Intent intent = new Intent(QuestionActivity.this, QuestionViewActivity.class);
			            	intent.putExtra("questionNo", Var.insertQuestionNo);
			            	intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.question, menu);
		return true;
	}

}
