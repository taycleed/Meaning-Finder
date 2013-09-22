package org.bigcamp4edu.meaningfinder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class QuestionViewActivity extends Activity {

	private Intent intent; // intent
	private String userId; // userId
	private String questionNo; // 질문번호

	private TextView star_name; // 별자리 한글이름
	private TextView star_name_en; // 별자리 영어이름
	private Drawable starDrawable;

	private ImageView question_star_image; // 질문에 해당하는 별자리 image view
	private TextView question_title; // 질문
	private TextView answer_text; // 답변

	private Button button_question_list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE); // 액션바 제거
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN); // 상태바 제거

		setContentView(R.layout.activity_question_view);

		intent = getIntent();
		userId = intent.getExtras().getString("userId");
		questionNo = intent.getExtras().getString("questionNo");
		Var.questionNo = questionNo;

		star_name = (TextView) findViewById(R.id.star_name);
		star_name_en = (TextView) findViewById(R.id.star_name_en);

		question_star_image = (ImageView) findViewById(R.id.question_star_image);
		question_title = (TextView) findViewById(R.id.question_title);
		answer_text = (TextView) findViewById(R.id.answer_text);

		button_question_list = (Button) findViewById(R.id.button_question_list);

		Thread x = new Thread(new Runnable() {

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
		starDrawable = setStar.getStarImage();
		question_star_image.setImageDrawable(starDrawable);

		question_title.setText(Var.info_question_name);
		answer_text.setText(Var.info_answer_name);

		button_question_list.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		Button setting_btn = (Button) findViewById(R.id.view_setting_btn);
		setting_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(QuestionViewActivity.this,
						SetupActivity.class);
				startActivity(intent);
			}
		});

	}

}
