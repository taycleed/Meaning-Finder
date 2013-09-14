package org.bigcamp4edu.meaningfinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class QuestionViewActivity extends Activity{
	
	private Intent intent;
	private String userId;
	private String questsionNo;
	
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
		questsionNo		= intent.getExtras().getString("questsionNo");
		
		
		
	}
}
