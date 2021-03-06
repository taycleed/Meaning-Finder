package org.bigcamp4edu.meaningfinder;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import org.bigcamp4edu.meaningfinder.LogoActivity;

import org.bigcamp4edu.meaningfinder.R;

import org.bigcamp4edu.meaningfinder.Var;

import org.bigcamp4edu.meaningfinder.DB;
import org.bigcamp4edu.meaningfinder.UrlPost;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class LogoActivity extends Activity implements OnClickListener,
		OnTouchListener {
	LogoActivity LogoActivity;
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	private Animation anim;

	private String tmp_email;

	private EditText email; // 이메일, 아이디 입력창
	private EditText password; // 비밀번호 입력창
	private ImageView user_id_del; // 아이디 삭제 버튼
	private ImageView user_pw_del; // 패스워드 삭제 버튼
	private Button loginButton; // 로그인 버튼
	private Button joinButton; // 회원가입 버튼

	private TextView loadingTitle; // 로딩중 문자
	private int titleNo = 0; // 로딩 문자번호
	private ArrayList<String> titleArr; // 로딩 문자 담는 배열

	private LinearLayout loginFormLayout; // 로그인 폼
	private Timer timer; // 타이머

	private SharedPreferences pref;

	private ProgressDialog progressDialog;

	private boolean fromQuestionActivity;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE); // 액션바 제거
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN); // 상태바 제거

		setContentView(R.layout.activity_logo);

		if (getIntent().getExtras() != null)
			fromQuestionActivity = getIntent().getExtras().getBoolean(
					"fromQuestion", false);

		LogoActivity = this;

		loadingTitle = (TextView) findViewById(R.id.loadingTitle); // 로딩중 문자
		loginFormLayout = (LinearLayout) findViewById(R.id.loginFormLayout); // 로그인
																				// 폼
		user_id_del = (ImageView) findViewById(R.id.user_id_del); // 아이디 삭제 버튼
		user_pw_del = (ImageView) findViewById(R.id.user_pw_del); // 패스워드 삭제 버튼
		loginButton = (Button) findViewById(R.id.button_login); // 로그인 버튼
		joinButton = (Button) findViewById(R.id.button_sign_up); // 회원가입 버튼

		findViewById(R.id.loading_layout).setOnTouchListener(LogoActivity);

		user_id_del.setOnClickListener(LogoActivity); // 아이디 삭제 버튼 리스너
		user_pw_del.setOnClickListener(LogoActivity); // 패스워드 삭제 버튼 리스너
		loginButton.setOnClickListener(LogoActivity); // 로그인 버튼 리스너
		joinButton.setOnClickListener(LogoActivity); // 회원가입 버튼 리스너

		Var.InitLoginInfo(this);

		loadingTitle.setVisibility(View.VISIBLE);
		loginFormLayout.setVisibility(View.GONE);

		progressDialog = new ProgressDialog(LogoActivity);

		email = (EditText) findViewById(R.id.email);
		password = (EditText) findViewById(R.id.password); // 패스워드 입력 창

		email.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) { // 아이디 입력 창 터치
																	// 시
					user_pw_del.setVisibility(View.INVISIBLE); // 패스워드 글자 삭제 버튼
																// 숨기기

					password.setBackgroundResource(R.drawable.input_normal);

					email.setCursorVisible(true);
					email.setBackgroundResource(R.drawable.input_focus);
					email.setPadding(
							(int) getResources().getDisplayMetrics().density * 10,
							0, 0, 0);

					if (email.getText().length() > 0) { // 글자 수가 0보다 클 때
						user_id_del.setVisibility(View.VISIBLE); // 삭제 버튼 보이기
					} else { // 아니면
						user_id_del.setVisibility(View.INVISIBLE); // 삭제 버튼 숨기기
					}
				}
				return false;
			}
		});

		email.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() > 0) { // 글자 수가 0보다 클 때
					user_id_del.setVisibility(View.VISIBLE); // 삭제 버튼 보이기
				} else { // 아니면
					user_id_del.setVisibility(View.INVISIBLE); // 삭제 버튼 숨기기
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		email.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_NEXT) { // 가상 키보드에서 다음 누를
																// 때

					user_id_del.setVisibility(View.INVISIBLE); // 아이디 글자 삭제버튼
																// 숨기기

					email.setBackgroundResource(R.drawable.input_normal);
					email.setPadding(
							(int) getResources().getDisplayMetrics().density * 10,
							0, 0, 0);

					password.setCursorVisible(true);
					password.setBackgroundResource(R.drawable.input_focus);
					password.setPadding((int) getResources()
							.getDisplayMetrics().density * 10, 0, 0, 0);
					password.requestFocus();
					return true;
				}
				return false;
			}
		});

		password.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) { // 입력창을 터치했을 때
					user_id_del.setVisibility(View.INVISIBLE); // 아이디 글자 삭제버튼
																// 숨기기

					email.setBackgroundResource(R.drawable.input_normal);

					password.setCursorVisible(true);
					password.setBackgroundResource(R.drawable.input_focus);
					password.setPadding((int) getResources()
							.getDisplayMetrics().density * 10, 0, 0, 0);

					if (password.getText().length() > 0) { // 글자 수가 0보다 클 때
						user_pw_del.setVisibility(View.VISIBLE); // 삭제 버튼 보이기
					} else { // 아니면
						user_pw_del.setVisibility(View.INVISIBLE); // 삭제 버튼 숨기기
					}
				}
				return false;
			}
		});
		password.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() > 0) { // 글자 수가 0보다 클 때
					user_pw_del.setVisibility(View.VISIBLE); // 삭제 버튼 보이기
				} else { // 아니면
					user_pw_del.setVisibility(View.INVISIBLE); // 삭제 버튼 숨기기
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		password.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) { // 가상 키보드에서 확인 누를
																// 때
					loginButton.performClick(); // 로그인 실행하기
					return true;
				}
				return false;
			}
		});

		titleArr = new ArrayList<String>();
		titleArr.add(getResources().getString(R.string.loading_title1));
		titleArr.add(getResources().getString(R.string.loading_title2));
		titleArr.add(getResources().getString(R.string.loading_title3));
		
		anim = AnimationUtils.loadAnimation(this, R.anim.alpha_1_1_1sec);
		anim.setRepeatCount(titleArr.size());
//		Log.d("VOM Logo", "Anim RepeatCount: " + Integer.toString(anim.getRepeatCount()) );
		anim.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
//				Log.d("VOM Logo", "Anim Start");
				
				loginFormLayout.setVisibility(View.GONE);
				loadingTitle.setVisibility(View.VISIBLE);
				titleNo = 0;
				if(titleNo < titleArr.size()){
					loadingTitle.setText(titleArr.get(titleNo++));
				}
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
//				Log.d("VOM Logo", "Anim Repeat");
				if(titleNo < titleArr.size()){
					loadingTitle.setText(titleArr.get(titleNo++));
				}
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
//				Log.d("VOM Logo", "Anim End");
				
				if(titleNo < titleArr.size()){
					// nothing
				}else{
					
					// 로그인 되어있을 경우
					if (Var.LOGIN_STATE) {
						Intent intent = new Intent(LogoActivity.this, ListActivity.class);
						startActivity(intent);
						finish();
					}else{
						loginFormLayout.setVisibility(View.VISIBLE);
						loadingTitle.setVisibility(View.GONE);
					}
				}
			}
		});

		
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		
		loadingTitle.startAnimation(anim);
	}

	/*******************************************************************************
	 * 
	 * 로그인 성공 시 프로그레스 다이얼로그를 없애고 로그인 성공 다이얼로그를 띄움
	 * 
	 ******************************************************************************/
	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int id = msg.what;

			if (progressDialog.isShowing()) { // 프로그레스 다이얼로그가 보이는가?
				progressDialog.cancel(); // 프로그레스 다이얼로그 멈춤
				progressDialog = null; // 프로그레스 다이얼로그 null
			}

			if (id == 0) {
				Var.LOGIN_STATE = false;
				onCreateDialog(DB.LOGIN_ERROR).show(); // 로그인 에러 다이얼로그 띄우기
			} else if (id == 1) {
				timer.cancel(); // 타이머 종료
				timer = null;

				pref = getSharedPreferences("Setting", 0);
				SharedPreferences.Editor edit = pref.edit();
				edit.putString("userId", Var.userId);
				edit.putString("userPw", Var.userPw);
				edit.putBoolean("LOGIN_STATE", Var.LOGIN_STATE);
				edit.commit();

				Var.LOGIN_STATE = true;

				// QuestionActivity에서 왔으면 거기로 보냄.
				if (fromQuestionActivity) {
					Intent intent = new Intent(LogoActivity.this,
							QuestionActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(LogoActivity.this,
							ListActivity.class);
					startActivity(intent);
				}
				finish(); // 로그인 액티비티 닫기
			}
		};
	};

	/*******************************************************************************
	 * 
	 * 로그 인 시 확인 다이얼로그
	 * 
	 ******************************************************************************/
	protected Dialog onCreateDialog(final int id) {
		Dialog dialog = null;
		AlertDialog.Builder builder = null;
		builder = new AlertDialog.Builder(LogoActivity);
		builder.setCancelable(true);

		if (id == DB.LOGIN_ERROR) { // 로그인 에러일 때
			builder.setMessage(getString(R.string.login_fail_msg))
					.setNeutralButton(getString(R.string.ok),
							new DialogInterface.OnClickListener() { // ok버튼&리스너
								public void onClick(DialogInterface dialog,
										int dialogid) {
									dialog.dismiss();
								}
							});
		}

		dialog = builder.create(); // 얼럿창 생성
		return dialog; // 얼럿창 리턴
	}

	/*******************************************************************************
	 * 
	 * 로그인 성공 시 url 보내기
	 * 
	 ******************************************************************************/
	private void loginSuccess(Context context) {
		Var.LOGIN_STATE = true; // 로그인 상태 참
	}

	/*******************************************************************************
	 * 
	 * 로그인 동작
	 * 
	 ******************************************************************************/
	boolean postLoginCheck(Context context) {
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>(); // 파라메터
																					// 값
																					// 보내기
																					// 위한
																					// 배열
		postParameters.add(new BasicNameValuePair("email", Var.userId));
		postParameters.add(new BasicNameValuePair("pass", Var.userPw));

		try {
			String response = UrlPost.executeHttpPost(DB.loginChkUrl,
					postParameters);
			String res = response.toString();
			String resultStart = "<result>";
			String resultEnd = "</result>";
			// String errorCodeStart = "<code>";
			// String errorCodeEnd = "</code>";
			String result = null;

			res = res.replaceAll("\\s+", "");
			// Log.i("TOKEN", res);
			try {
				result = res.substring(
						res.indexOf(resultStart) + resultStart.length(),
						res.indexOf(resultEnd));
				// Log.i("TOKEN", result);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (result.equals("true")) { // 성공 메시지일 경우
				loginSuccess(context);
				return true;
			} else { // 성공이 아닐 경우
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
	 * 로그인 프로세스 (로그인 버튼 눌렀을 때만 실행해야 함)
	 * 
	 ******************************************************************************/
	void loginProcess(String str) {
		Var.userId = email.getText().toString(); // 아이디 텍스트창에서 아이디값 받기
		Var.userPw = password.getText().toString(); // 비밀번호 텍스트창에서 비밀번호값 받기
		Var.LOGIN_PAGE = true;

		if (str != DB.AUTOLOGIN) { // 자동로그인이 아닐 때
			progressDialog = ProgressDialog.show(LogoActivity, null,
					getString(R.string.login_wait));
		}

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				if (postLoginCheck(LogoActivity)) { // 로그인 체크
					TimerTask task = new TimerTask() { // c2dm 핸들러 체크를 하기 위해
						public void run() {
							handler.sendEmptyMessage(1); // 핸들러에 1번 메시지 보내기
						}
					};
					timer = new Timer();
					timer.schedule(task, 100, 1000);
				} else { // 로그인 성공이 아니면
					handler.sendEmptyMessage(0); // 핸들러에 0번 메시지 보내기
				}
			}
		});
		thread.start();
		thread = null;
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		InputMethodManager imm = (InputMethodManager) LogoActivity
				.getSystemService(Context.INPUT_METHOD_SERVICE); // 가상키보드 설정
		imm.hideSoftInputFromWindow(email.getWindowToken(), 0); // 가상키보드 숨기기

		user_id_del.setVisibility(View.INVISIBLE); // 아이디 글자 삭제버튼 숨기기
		user_pw_del.setVisibility(View.INVISIBLE); // 아이디 글자 삭제버튼 숨기기

		email.setCursorVisible(false);
		password.setCursorVisible(false);

		email.setBackgroundResource(R.drawable.input_normal);
		email.setPadding((int) getResources().getDisplayMetrics().density * 10,
				0, 0, 0);
		email.clearFocus();

		password.setBackgroundResource(R.drawable.input_normal);
		password.setPadding(
				(int) getResources().getDisplayMetrics().density * 10, 0, 0, 0);
		password.clearFocus();

		Var.FINISH = false;
		return false;
	}

	/*******************************************************************************
	 * 
	 * 버튼 클릭 시
	 * 
	 ******************************************************************************/
	@Override
	public void onClick(View v) {
		int id = v.getId();

		if (id == R.id.button_login) { // 로그인 버튼
			loginProcess(null);
			// customWebViewActivity.customWebView.goBackOrForward(-2);
		} else if (id == R.id.user_id_del) { // 아이디 글자 삭제 버튼
			email.setText("");
			user_id_del.setVisibility(View.GONE);
		} else if (id == R.id.user_pw_del) { // 패스워드 글자 삭제 버튼
			password.setText("");
			user_pw_del.setVisibility(View.GONE);
		} else if (id == R.id.button_sign_up) { // 회원가입 버튼
			Intent intent = new Intent(LogoActivity.this, JoinActivity.class);
			startActivityForResult(intent, 1);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {

				tmp_email = data.getExtras().getString("tmp_email");

				Log.i("TMPEMAIL", tmp_email);
				email.setText(tmp_email);
			}
			break;

		default:
			break;
		}

	}

}
