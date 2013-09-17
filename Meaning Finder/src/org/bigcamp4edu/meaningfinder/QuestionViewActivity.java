package org.bigcamp4edu.meaningfinder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class QuestionViewActivity extends Activity{
	
	private Intent 		intent;							// intent
	private String 		userId;							// userId
	private String 		questionNo;						// ������ȣ
	
	private TextView	star_name;						// ���ڸ� �ѱ��̸�
	private TextView	star_name_en;						// ���ڸ� �����̸�
	private Drawable 	starDrawable;
	
	private ImageView 	question_star_image;			// ������ �ش��ϴ� ���ڸ� image view
	private TextView 	question_title;					// ����
	private TextView 	answer_text;					// �亯
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);								// �׼ǹ� ����
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);					// ���¹� ����
		
		setContentView(R.layout.activity_question_view);
		
		intent 			= getIntent();		
		userId			= intent.getExtras().getString("userId");
		questionNo		= intent.getExtras().getString("questionNo");
		Var.questionNo	= questionNo;
		
		star_name			= (TextView) 	findViewById(R.id.star_name);
		star_name_en		= (TextView)	findViewById(R.id.star_name_en);
		
		question_star_image	= (ImageView)	findViewById(R.id.question_star_image);
		question_title		= (TextView) 	findViewById(R.id.question_title);
		answer_text			= (TextView) 	findViewById(R.id.answer_text);
		
		Thread x	= new Thread(new Runnable() {
			
			@Override
			public void run() {
				XmlParser.getAnswerText();
			}
		});
		x.start();
		
		try {
			x.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		star_name.setText(Var.info_star_name);
		star_name_en.setText(Var.info_star_name_en);
		
		setStarImage setStar = new setStarImage(this);
		setStar.setStarImage(Var.info_star_img);
		starDrawable	= setStar.getStarImage();
		question_star_image.setImageDrawable(starDrawable);
		
		question_title.setText(Var.info_question_name);
		answer_text.setText(Var.info_answer_name);
		
		

	}
	
	
	
	
	
	
	
	
	
}
