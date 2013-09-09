package org.bigcamp4edu.meaningfinder;

import java.util.ArrayList;

import android.R.string;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JoinActivity extends Activity {
	JoinActivity JoinActivity;
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";
	
    private boolean isSubmittable;
    
    Thread x; 								// 쓰레드
	Handler mHandler;						// 핸들러
	

	Button		button_join_submit;			// 회원가입 버튼
	
	EditText 	name;						// 이름 입력창
	EditText 	birthday;					// 생일 입력창
	EditText 	email;						// 이메일 입력창
	EditText 	pwd;						// 비밀번호 입력창
	EditText 	pwdconfirm;					// 비밀번호 확인 입력창
	
	String tmp_name, tmp_birthday, tmp_email, tmp_pwd; 
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		requestWindowFeature(Window.FEATURE_NO_TITLE);								// 액션바 제거
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);						// 상태바 제거
        
        setContentView(R.layout.activity_join);
        
        JoinActivity	= this;
        

    	button_join_submit	= (Button) findViewById(R.id.button_join_submit);			// 회원가입 버튼
    	name				= (EditText) findViewById(R.id.name);						// 이름 입력 뷰
    	birthday			= (EditText) findViewById(R.id.birthday);					// 생일 입력 뷰
    	email				= (EditText) findViewById(R.id.email);						// 이메일 입력 뷰
    	pwd					= (EditText) findViewById(R.id.pwd);						// 비밀번호 입력 뷰
    	pwdconfirm			= (EditText) findViewById(R.id.pwd_confirm);				// 비밀번호 확인 입력 뷰
    	
    	
    	button_join_submit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				
				tmp_name		= name.getText().toString();
				tmp_birthday	= birthday.getText().toString();
				tmp_email		= email.getText().toString();
				tmp_pwd			= pwd.getText().toString();
				
//				 Log.d("bigcamp4edu", tmp_name + "/" + tmp_birthday + "/" + tmp_email + "/" + tmp_pwd);
				
				Thread thread = new Thread(new Runnable() {
					public void run() {
						 if(postJoinCheck(JoinActivity))
						 {
							 Bundle extra = new Bundle();
							 Intent intent = new Intent();
							 
							 extra.putString("tmp_email", tmp_email);

							 intent.putExtras(extra);
							 setResult(RESULT_OK, intent);
					
							 finish();
						 }
					}
				});
				thread.start();
				thread = null;
				
			}
		});

    }
    
    
    
    /*******************************************************************************
	 * 
	 *	회원가입 동작
	 *
	 ******************************************************************************/
	boolean postJoinCheck(Context context){
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();	// 파라메터 값 보내기 위한 배열
    	postParameters.add(new BasicNameValuePair("name",		tmp_name));
    	postParameters.add(new BasicNameValuePair("birthday",	tmp_birthday));
    	postParameters.add(new BasicNameValuePair("email",		tmp_email));
    	postParameters.add(new BasicNameValuePair("pass",		tmp_pwd));
    	
    	
    	try {

    	    String response			= UrlPost.executeHttpPost(DB.joinChkUrl, postParameters);
    	    Log.i("test", response);
    	    String res				= response.toString();
    	    String resultStart		= "<result>";
    	    String resultEnd		= "</result>";
    	    String errorCodeStart	= "<code>";
    	    String errorCodeEnd		= "</code>";
    	    String result			= null;
    	    
    	    res = res.replaceAll("\\s+", "");
    	    try{
    	    	result = res.substring(res.indexOf(resultStart)+resultStart.length(), res.indexOf(resultEnd));
//    	    	Log.i("TOKEN", result);
    	    }catch(Exception e){
    	    	e.printStackTrace();
    	    }
    	    
    	    if(result.equals("true")){												// 성공 메시지일 경우
    	    	return true;
    	    }else{																	// 성공이 아닐 경우
    			
    	    	return false;
    	    }
    	} catch (Exception e) {
    		e.printStackTrace();
    		return false;
    	}
	}
    
    
    
    
    public boolean onTouch(View arg0, MotionEvent arg1) {
		InputMethodManager imm = (InputMethodManager) JoinActivity.getSystemService(Context.INPUT_METHOD_SERVICE);	// 가상키보드 설정
		imm.hideSoftInputFromWindow(email.getWindowToken(), 0);											// 가상키보드 숨기기
		
		
		Var.FINISH = false;
		return false;
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.join, menu);
        return true;
    }
    
}
