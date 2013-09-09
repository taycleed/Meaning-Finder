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

	private Thread x; 								// ������
	private Handler mHandler;						// �ڵ鷯
	
	private String tmp_email;
	
	private EditText email; 						// �̸���, ���̵� �Է�â
	private EditText password; 						// ��й�ȣ �Է�â
	private ImageView user_id_del; 					// ���̵� ���� ��ư
	private ImageView user_pw_del; 					// �н����� ���� ��ư
	private Button loginButton;						// �α��� ��ư
	private Button joinButton; 						// ȸ������ ��ư

	private TextView loadingTitle;					// �ε��� ����
	private int	titleNo	= 0;						// �ε� ���ڹ�ȣ
	private ArrayList<String> titleArr;				// �ε� ���� ��� �迭
	
	private LinearLayout loginFormLayout; 			// �α��� ��
	private Timer			timer;					// Ÿ�̸�
	
	private ProgressDialog progressDialog;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);								// �׼ǹ� ����
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);						// ���¹� ����
		
		setContentView(R.layout.activity_logo);
		
		LogoActivity	= this;
		
		loadingTitle	= (TextView) findViewById(R.id.loadingTitle);				// �ε��� ����
		loginFormLayout	= (LinearLayout) findViewById(R.id.loginFormLayout);		// �α��� ��
		user_id_del		= (ImageView) findViewById(R.id.user_id_del);				// ���̵� ���� ��ư
		user_pw_del		= (ImageView) findViewById(R.id.user_pw_del);				// �н����� ���� ��ư
		loginButton		= (Button) findViewById(R.id.button_login);					// �α��� ��ư
		joinButton		= (Button) findViewById(R.id.button_sign_up);				// ȸ������ ��ư
		
		findViewById(R.id.loading_layout).setOnTouchListener(LogoActivity);
		
		user_id_del.setOnClickListener(LogoActivity);								// ���̵� ���� ��ư ������
		user_pw_del.setOnClickListener(LogoActivity);								// �н����� ���� ��ư ������
		loginButton.setOnClickListener(LogoActivity);								// �α��� ��ư ������
		joinButton.setOnClickListener(LogoActivity);								// ȸ������ ��ư ������
		
		mHandler		 = new Handler();
		
		// �α����� �ȵ��������
		if(!Var.LOGIN_STATE)
		{
			loadingTitle.setVisibility(View.VISIBLE);
			loginFormLayout.setVisibility(View.GONE);
			
			progressDialog = new ProgressDialog(LogoActivity);
			
			email	= (EditText) findViewById(R.id.email);
			email.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if(event.getAction() == KeyEvent.ACTION_DOWN){					// ���̵� �Է� â ��ġ ��
						user_pw_del.setVisibility(View.INVISIBLE);					// �н����� ���� ���� ��ư �����
						
						password.setBackgroundResource(R.drawable.input_normal);
						

						email.setCursorVisible(true);
						email.setBackgroundResource(R.drawable.input_focus);
						email.setPadding((int) getResources().getDisplayMetrics().density*10, 0, 0, 0);
						
						
						if(email.getText().length() > 0){						// ���� ���� 0���� Ŭ ��
							user_id_del.setVisibility(View.VISIBLE);				// ���� ��ư ���̱�
						}else{														// �ƴϸ�
							user_id_del.setVisibility(View.INVISIBLE);				// ���� ��ư �����
						}
					}
					return false;
				}
			});
			
			email.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					if(s.length() > 0){												// ���� ���� 0���� Ŭ ��
						user_id_del.setVisibility(View.VISIBLE);					// ���� ��ư ���̱�
					}else{															// �ƴϸ�
						user_id_del.setVisibility(View.INVISIBLE);					// ���� ��ư �����
					}
				}
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,	int after) {
				}
				@Override
				public void afterTextChanged(Editable s) {
				}
			});
			
			
			password		= (EditText) findViewById(R.id.password);				// �н����� �Է� â
			password.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if(event.getAction() == KeyEvent.ACTION_DOWN){					// �Է�â�� ��ġ���� ��
						user_id_del.setVisibility(View.INVISIBLE);					// ���̵� ���� ������ư �����
						
						email.setBackgroundResource(R.drawable.input_normal);
						

						password.setCursorVisible(true);
						password.setBackgroundResource(R.drawable.input_focus);
						password.setPadding((int) getResources().getDisplayMetrics().density*10, 0, 0, 0);
						
						if(password.getText().length() > 0){						// ���� ���� 0���� Ŭ ��
							user_pw_del.setVisibility(View.VISIBLE);				// ���� ��ư ���̱�
						}else{														// �ƴϸ�
							user_pw_del.setVisibility(View.INVISIBLE);				// ���� ��ư �����
						}
					}
					return false;
				}
			});
			password.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					if(s.length() > 0){												// ���� ���� 0���� Ŭ ��
						user_pw_del.setVisibility(View.VISIBLE);					// ���� ��ư ���̱�
					}else{															// �ƴϸ�
						user_pw_del.setVisibility(View.INVISIBLE);					// ���� ��ư �����
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
					if(actionId == EditorInfo.IME_ACTION_DONE){						// ���� Ű���忡�� Ȯ�� ���� ��
						loginButton.performClick();									// �α��� �����ϱ�
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
	 *	�α��� ���� �� ���α׷��� ���̾�α׸� ���ְ� �α��� ���� ���̾�α׸� ���
	 *
	 ******************************************************************************/
	public Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			int id = msg.what;
			
			if(progressDialog.isShowing()){											// ���α׷��� ���̾�αװ� ���̴°�?
				progressDialog.cancel();											// ���α׷��� ���̾�α� ����
				progressDialog = null;												// ���α׷��� ���̾�α� null
			}

			if(id == 0){
				Var.LOGIN_STATE = false;
				onCreateDialog(DB.LOGIN_ERROR).show();								// �α��� ���� ���̾�α� ����
			}else if(id == 1){
				timer.cancel();														// Ÿ�̸� ����
				timer = null;
				Var.LOGIN_STATE = true;
				Intent intent = new Intent(LogoActivity.this, ListActivity.class);
                startActivityForResult(intent, 1);
				finish();															// �α��� ��Ƽ��Ƽ �ݱ�
			}
		};
	};
	
	/*******************************************************************************
	 * 
	 * 	�α� �� �� Ȯ�� ���̾�α�
	 * 
	 ******************************************************************************/
	protected Dialog onCreateDialog(final int id){
    	Dialog				dialog	= null;
		AlertDialog.Builder builder	= null;
		builder = new AlertDialog.Builder(LogoActivity);
		builder.setCancelable(true);
		
		if(id == DB.LOGIN_ERROR){													// �α��� ������ ��
			builder.setMessage(getString(R.string.login_fail_msg))
				.setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {	// ok��ư&������
					public void onClick(DialogInterface dialog, int dialogid) {
						dialog.dismiss();
					}
			});
		}
		
		dialog = builder.create();													// ��â ����
		return dialog;																// ��â ����
	}
	
	
	/*******************************************************************************
	 * 
	 * 	�α��� ���� �� url ������
	 * 
	 ******************************************************************************/
	private void loginSuccess(Context context){
		Var.LOGIN_STATE = true;														// �α��� ���� ��
	}
	
	
	/*******************************************************************************
	 * 
	 *	�α��� ����
	 *
	 ******************************************************************************/
	boolean postLoginCheck(Context context){
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();	// �Ķ���� �� ������ ���� �迭
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
    	    
    	    if(result.equals("true")){												// ���� �޽����� ���
    	    	loginSuccess(context);
    	    	return true;
    	    }else{																	// ������ �ƴ� ���
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
	 * 	�α��� ���μ��� (�α��� ��ư ������ ���� �����ؾ� ��)
	 * 
	 ******************************************************************************/
	void loginProcess(String str){
    	Var.userId = email.getText().toString();								// ���̵� �ؽ�Ʈâ���� ���̵� �ޱ�
    	Var.userPw = password.getText().toString();								// ��й�ȣ �ؽ�Ʈâ���� ��й�ȣ�� �ޱ�
		Var.LOGIN_PAGE = true;
		
		if(str != DB.AUTOLOGIN){													// �ڵ��α����� �ƴ� ��
			progressDialog = ProgressDialog.show(LogoActivity, null, getString(R.string.login_wait));
		}
		
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				if(postLoginCheck(LogoActivity)){										// �α��� üũ
					TimerTask task = new TimerTask(){								// c2dm �ڵ鷯 üũ�� �ϱ� ����
						public void run(){
							handler.sendEmptyMessage(1);							// �ڵ鷯�� 1�� �޽��� ������
						}
					};
					timer = new Timer();
					timer.schedule(task, 100, 1000);
				}else{																// �α��� ������ �ƴϸ�
					handler.sendEmptyMessage(0);									// �ڵ鷯�� 0�� �޽��� ������
				}
			}
		});
		thread.start();
		thread = null;
	}
	
	
	

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		InputMethodManager imm = (InputMethodManager) LogoActivity.getSystemService(Context.INPUT_METHOD_SERVICE);	// ����Ű���� ����
		imm.hideSoftInputFromWindow(email.getWindowToken(), 0);											// ����Ű���� �����
		
		user_id_del.setVisibility(View.INVISIBLE);					// ���̵� ���� ������ư �����
		user_pw_del.setVisibility(View.INVISIBLE);					// ���̵� ���� ������ư �����
		
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
	 *	��ư Ŭ�� ��
	 *
	 ******************************************************************************/
	@Override
	public void onClick(View v) {
		int id = v.getId();
		
		if(id == R.id.button_login){													// �α��� ��ư
			loginProcess(null);
			//customWebViewActivity.customWebView.goBackOrForward(-2);
		}else if(id == R.id.user_id_del){											// ���̵� ���� ���� ��ư
			email.setText("");
			user_id_del.setVisibility(View.GONE);
		}else if(id == R.id.user_pw_del){											// �н����� ���� ���� ��ư
			password.setText("");
			user_pw_del.setVisibility(View.GONE);
		}else if(id == R.id.button_sign_up){												// ȸ������ ��ư
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
