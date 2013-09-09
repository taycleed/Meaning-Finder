package org.bigcamp4edu.meaningfinder;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EncodingUtils;

import android.R.array;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.bigcamp4edu.meaningfinder.LoginActivity.UserLoginTask;
import org.bigcamp4edu.meaningfinder.util.SystemUiHider;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import org.bigcamp4edu.meaningfinder.LogoActivity;

import org.bigcamp4edu.meaningfinder.R;

import org.bigcamp4edu.meaningfinder.Var;

import org.bigcamp4edu.meaningfinder.DB;
import org.bigcamp4edu.meaningfinder.UrlPost;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LogoActivity extends Activity implements OnClickListener,OnTouchListener {
	LogoActivity LogoActivity;
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	private Thread x; 								// 쓰레드
	private Handler mHandler;						// 핸들러
	
	private String tmp_email;
	
	private EditText email; 						// 이메일, 아이디 입력창
	private EditText password; 						// 비밀번호 입력창
	private ImageView user_id_del; 					// 아이디 삭제 버튼
	private ImageView user_pw_del; 					// 패스워드 삭제 버튼
	private Button loginButton;						// 로그인 버튼
	private Button joinButton; 						// 회원가입 버튼

	private TextView loadingTitle;					// 로딩중 문자
	private int	titleNo	= 0;						// 로딩 문자번호
	private ArrayList<String> titleArr;				// 로딩 문자 담는 배열
	
	private LinearLayout loginFormLayout; 			// 로그인 폼
	private Timer			timer;					// 타이머
	
	private ProgressDialog progressDialog;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);								// 액션바 제거
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);						// 상태바 제거
		
		setContentView(R.layout.activity_logo);
		
		LogoActivity	= this;
		
		loadingTitle	= (TextView) findViewById(R.id.loadingTitle);				// 로딩중 문자
		loginFormLayout	= (LinearLayout) findViewById(R.id.loginFormLayout);		// 로그인 폼
		user_id_del		= (ImageView) findViewById(R.id.user_id_del);				// 아이디 삭제 버튼
		user_pw_del		= (ImageView) findViewById(R.id.user_pw_del);				// 패스워드 삭제 버튼
		loginButton		= (Button) findViewById(R.id.button_login);					// 로그인 버튼
		joinButton		= (Button) findViewById(R.id.button_sign_up);				// 회원가입 버튼
		
		findViewById(R.id.loading_layout).setOnTouchListener(LogoActivity);
		
		user_id_del.setOnClickListener(LogoActivity);								// 아이디 삭제 버튼 리스너
		user_pw_del.setOnClickListener(LogoActivity);								// 패스워드 삭제 버튼 리스너
		loginButton.setOnClickListener(LogoActivity);								// 로그인 버튼 리스너
		joinButton.setOnClickListener(LogoActivity);								// 회원가입 버튼 리스너
		
		mHandler		 = new Handler();
		
		// 로그인이 안되있을경우
		if(!Var.LOGIN_STATE)
		{
			loadingTitle.setVisibility(View.VISIBLE);
			loginFormLayout.setVisibility(View.GONE);
			
			progressDialog = new ProgressDialog(LogoActivity);
			
			email	= (EditText) findViewById(R.id.email);
			email.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if(event.getAction() == KeyEvent.ACTION_DOWN){					// 아이디 입력 창 터치 시
						user_pw_del.setVisibility(View.INVISIBLE);					// 패스워드 글자 삭제 버튼 숨기기
						
						password.setBackgroundResource(R.drawable.input_normal);
						

						email.setCursorVisible(true);
						email.setBackgroundResource(R.drawable.input_focus);
						email.setPadding((int) getResources().getDisplayMetrics().density*10, 0, 0, 0);
						
						
						if(email.getText().length() > 0){						// 글자 수가 0보다 클 때
							user_id_del.setVisibility(View.VISIBLE);				// 삭제 버튼 보이기
						}else{														// 아니면
							user_id_del.setVisibility(View.INVISIBLE);				// 삭제 버튼 숨기기
						}
					}
					return false;
				}
			});
			
			email.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					if(s.length() > 0){												// 글자 수가 0보다 클 때
						user_id_del.setVisibility(View.VISIBLE);					// 삭제 버튼 보이기
					}else{															// 아니면
						user_id_del.setVisibility(View.INVISIBLE);					// 삭제 버튼 숨기기
					}
				}
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,	int after) {
				}
				@Override
				public void afterTextChanged(Editable s) {
				}
			});
			
			
			password		= (EditText) findViewById(R.id.password);				// 패스워드 입력 창
			password.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if(event.getAction() == KeyEvent.ACTION_DOWN){					// 입력창을 터치했을 때
						user_id_del.setVisibility(View.INVISIBLE);					// 아이디 글자 삭제버튼 숨기기
						
						email.setBackgroundResource(R.drawable.input_normal);
						

						password.setCursorVisible(true);
						password.setBackgroundResource(R.drawable.input_focus);
						password.setPadding((int) getResources().getDisplayMetrics().density*10, 0, 0, 0);
						
						if(password.getText().length() > 0){						// 글자 수가 0보다 클 때
							user_pw_del.setVisibility(View.VISIBLE);				// 삭제 버튼 보이기
						}else{														// 아니면
							user_pw_del.setVisibility(View.INVISIBLE);				// 삭제 버튼 숨기기
						}
					}
					return false;
				}
			});
			password.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					if(s.length() > 0){												// 글자 수가 0보다 클 때
						user_pw_del.setVisibility(View.VISIBLE);					// 삭제 버튼 보이기
					}else{															// 아니면
						user_pw_del.setVisibility(View.INVISIBLE);					// 삭제 버튼 숨기기
					}
				}
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,	int after) {
				}
				@Override
				public void afterTextChanged(Editable s) {
				}
			});
			
			password.setOnEditorActionListener(new OnEditorActionListener() {
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					if(actionId == EditorInfo.IME_ACTION_DONE){						// 가상 키보드에서 확인 누를 때
						loginButton.performClick();									// 로그인 실행하기
						return true;
					}
					return false;
				}
			});
			
			
			
			
			
			
			x = new Thread(new Runnable()
			{
				@Override
				public void run() {
					// TODO Auto-generated method stub
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							try {
								changeTitle();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					});
				}

				private void changeTitle() throws InterruptedException 
				{
					titleArr = new ArrayList<String>();
					
					String strFormat1 = getResources().getString(R.string.loading_title1);
					String strFormat2 = getResources().getString(R.string.loading_title2);
					String strFormat3 = getResources().getString(R.string.loading_title3);
					
					titleArr.add(strFormat1);
					titleArr.add(strFormat2);
					titleArr.add(strFormat3);
					
					new CountDownTimer(4000, 1000)
					{
						public void onTick(long millisUntilFinished)
						{
							final String strFormat = (String) titleArr.get(titleNo++);
							loadingTitle.setText(strFormat);

							if (titleNo == titleArr.size())
							{
								titleNo = 0;
							}
						}

						public void onFinish()
						{
							// mTextField.setText("done!");
							loginFormLayout.setVisibility(View.VISIBLE);
							loadingTitle.setVisibility(View.GONE);
							x = null;
						}
						
					}.start();

				}
			});
			x.start();
			
		}
		
	}
	
	
	/*******************************************************************************
	 * 
	 *	로그인 성공 시 프로그레스 다이얼로그를 없애고 로그인 성공 다이얼로그를 띄움
	 *
	 ******************************************************************************/
	public Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			int id = msg.what;
			
			if(progressDialog.isShowing()){											// 프로그레스 다이얼로그가 보이는가?
				progressDialog.cancel();											// 프로그레스 다이얼로그 멈춤
				progressDialog = null;												// 프로그레스 다이얼로그 null
			}

			if(id == 0){
				Var.LOGIN_STATE = false;
				onCreateDialog(DB.LOGIN_ERROR).show();								// 로그인 에러 다이얼로그 띄우기
			}else if(id == 1){
				timer.cancel();														// 타이머 종료
				timer = null;
				Var.LOGIN_STATE = true;
				Intent intent = new Intent(LogoActivity.this, ListActivity.class);
                startActivityForResult(intent, 1);
				finish();															// 로그인 액티비티 닫기
			}
		};
	};
	
	/*******************************************************************************
	 * 
	 * 	로그 인 시 확인 다이얼로그
	 * 
	 ******************************************************************************/
	protected Dialog onCreateDialog(final int id){
    	Dialog				dialog	= null;
		AlertDialog.Builder builder	= null;
		builder = new AlertDialog.Builder(LogoActivity);
		builder.setCancelable(true);
		
		if(id == DB.LOGIN_ERROR){													// 로그인 에러일 때
			builder.setMessage(getString(R.string.login_fail_msg))
				.setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {	// ok버튼&리스너
					public void onClick(DialogInterface dialog, int dialogid) {
						dialog.dismiss();
					}
			});
		}
		
		dialog = builder.create();													// 얼럿창 생성
		return dialog;																// 얼럿창 리턴
	}
	
	
	/*******************************************************************************
	 * 
	 * 	로그인 성공 시 url 보내기
	 * 
	 ******************************************************************************/
	private void loginSuccess(Context context){
		Var.LOGIN_STATE = true;														// 로그인 상태 참
	}
	
	
	/*******************************************************************************
	 * 
	 *	로그인 동작
	 *
	 ******************************************************************************/
	boolean postLoginCheck(Context context){
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();	// 파라메터 값 보내기 위한 배열
    	postParameters.add(new BasicNameValuePair("email",	Var.userId));
    	postParameters.add(new BasicNameValuePair("pass",	Var.userPw));

    	try {
    	    String response			= UrlPost.executeHttpPost(DB.loginChkUrl, postParameters);
    	    String res				= response.toString();
    	    String resultStart		= "<result>";
    	    String resultEnd		= "</result>";
    	    String errorCodeStart	= "<code>";
    	    String errorCodeEnd		= "</code>";
    	    String result			= null;
    	    
    	    res = res.replaceAll("\\s+", "");
//    	    Log.i("TOKEN", res);
    	    try{
    	    	result = res.substring(res.indexOf(resultStart)+resultStart.length(), res.indexOf(resultEnd));
//    	    	Log.i("TOKEN", result);
    	    }catch(Exception e){
    	    	e.printStackTrace();
    	    }
    	    
    	    if(result.equals("true")){												// 성공 메시지일 경우
    	    	loginSuccess(context);
    	    	return true;
    	    }else{																	// 성공이 아닐 경우
    			Var.LOGIN_PAGE = false;
    	    	return false;
    	    }
    	} catch (Exception e) {
    		e.printStackTrace();
    		return false;
    	}
	}
	
	
	
	/*******************************************************************************
	 * 
	 * 	로그인 프로세스 (로그인 버튼 눌렀을 때만 실행해야 함)
	 * 
	 ******************************************************************************/
	void loginProcess(String str){
    	Var.userId = email.getText().toString();								// 아이디 텍스트창에서 아이디값 받기
    	Var.userPw = password.getText().toString();								// 비밀번호 텍스트창에서 비밀번호값 받기
		Var.LOGIN_PAGE = true;
		
		if(str != DB.AUTOLOGIN){													// 자동로그인이 아닐 때
			progressDialog = ProgressDialog.show(LogoActivity, null, getString(R.string.login_wait));
		}
		
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				if(postLoginCheck(LogoActivity)){										// 로그인 체크
					TimerTask task = new TimerTask(){								// c2dm 핸들러 체크를 하기 위해
						public void run(){
							handler.sendEmptyMessage(1);							// 핸들러에 1번 메시지 보내기
						}
					};
					timer = new Timer();
					timer.schedule(task, 100, 1000);
				}else{																// 로그인 성공이 아니면
					handler.sendEmptyMessage(0);									// 핸들러에 0번 메시지 보내기
				}
			}
		});
		thread.start();
		thread = null;
	}
	
	
	

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		InputMethodManager imm = (InputMethodManager) LogoActivity.getSystemService(Context.INPUT_METHOD_SERVICE);	// 가상키보드 설정
		imm.hideSoftInputFromWindow(email.getWindowToken(), 0);											// 가상키보드 숨기기
		
		user_id_del.setVisibility(View.INVISIBLE);					// 아이디 글자 삭제버튼 숨기기
		user_pw_del.setVisibility(View.INVISIBLE);					// 아이디 글자 삭제버튼 숨기기
		
		email.setCursorVisible(false);
		password.setCursorVisible(false);
		
		email.setBackgroundResource(R.drawable.input_normal);
		email.setPadding((int) getResources().getDisplayMetrics().density*10, 0, 0, 0);
		email.clearFocus();
		
		password.setBackgroundResource(R.drawable.input_normal);
		password.setPadding((int) getResources().getDisplayMetrics().density*10, 0, 0, 0);
		password.clearFocus();
		
		Var.FINISH = false;
		return false;
	}

	/*******************************************************************************
	 * 
	 *	버튼 클릭 시
	 *
	 ******************************************************************************/
	@Override
	public void onClick(View v) {
		int id = v.getId();
		
		if(id == R.id.button_login){													// 로그인 버튼
			loginProcess(null);
			//customWebViewActivity.customWebView.goBackOrForward(-2);
		}else if(id == R.id.user_id_del){											// 아이디 글자 삭제 버튼
			email.setText("");
			user_id_del.setVisibility(View.GONE);
		}else if(id == R.id.user_pw_del){											// 패스워드 글자 삭제 버튼
			password.setText("");
			user_pw_del.setVisibility(View.GONE);
		}else if(id == R.id.button_sign_up){												// 회원가입 버튼
			Intent intent = new Intent(LogoActivity.this, JoinActivity.class);
			 startActivityForResult(intent, 1);
		}
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case 1:
			if(resultCode == RESULT_OK)
			{
				
				tmp_email	= data.getExtras().getString("tmp_email");
				
				Log.i("TMPEMAIL", tmp_email);
				email.setText(tmp_email);
			}
			break;

		default:
			break;
		}
		
	}
	

}
