package org.bigcamp4edu.meaningfinder;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class JoinActivity extends Activity {
	JoinActivity JoinActivity;
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";
	
//    private boolean isSubmittable;
    
    Thread x; 								// 쓰레드
	Handler mHandler;						// 핸들러
	

	Button		button_join_submit;			// 회원가입 버튼
	
	EditText 	name;						// 이름 입력창
	TextView	birthday;					// 생일 창
	RadioGroup	sex;						// 성별 입력버튼 그룹
	EditText 	email;						// 이메일 입력창
	EditText 	pwd;						// 비밀번호 입력창
	EditText 	pwdconfirm;					// 비밀번호 확인 입력창
	
	String tmp_name ="", tmp_birthday ="", tmp_email ="", tmp_pwd ="", tmp_sex =""; 
	int init_year, init_month, init_day;
    
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
    	birthday			= (TextView) findViewById(R.id.textView_join_birthday);
    	sex					= (RadioGroup) findViewById(R.id.radioGroup_join_sex);
    	email				= (EditText) findViewById(R.id.email);						// 이메일 입력 뷰
    	pwd					= (EditText) findViewById(R.id.pwd);						// 비밀번호 입력 뷰
    	pwdconfirm			= (EditText) findViewById(R.id.pwd_confirm);				// 비밀번호 확인 입력 뷰
    	
		TextWatcher joinInfoTextWatcher = new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {};
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,	int after) {};
			@Override
			public void afterTextChanged(Editable s) {
				JoinActivity.this.CheckJoinInfo();
			};
		};
    	name.addTextChangedListener(joinInfoTextWatcher);
    	birthday.addTextChangedListener(joinInfoTextWatcher);
    	email.addTextChangedListener(joinInfoTextWatcher);
    	pwd.addTextChangedListener(joinInfoTextWatcher);
    	pwdconfirm.addTextChangedListener(joinInfoTextWatcher);
    	sex.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId == R.id.radio_sex_male){
//					Toast.makeText(JoinActivity.this, "남자", Toast.LENGTH_SHORT).show();
					Log.d("VOM Join", "Selected MALE");
					tmp_sex = "male";
				}else if(checkedId == R.id.radio_sex_female){
//					Toast.makeText(JoinActivity.this, "여자", Toast.LENGTH_SHORT).show();
					Log.d("VOM Join", "Selected FEMALE");
					tmp_sex = "female";
				}
			}
		});
    	
    	
    	init_year = 1997;
    	init_month = 0;
    	init_day = 1;
    	birthday.setOnTouchListener(new OnTouchListener() {
    		private OnDateSetListener onDateSet = new OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					init_year = year;
					init_month = monthOfYear;
					init_day = dayOfMonth;
					tmp_birthday = String.format("%04d%02d%02d", year, monthOfYear + 1, dayOfMonth);
					birthday.setText(String.format("%04d년 %d월 %d일", year, monthOfYear + 1, dayOfMonth));
				}
			};
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					DatePickerDialog datePicker = new DatePickerDialog(JoinActivity.this, onDateSet, init_year, init_month, init_day);
					datePicker.getDatePicker().setCalendarViewShown(false);
					datePicker.show();
				}
				return true;
			}
		});
    	birthday.setOnClickListener(new OnClickListener() {
    		
    		private OnDateSetListener onDateSet = new OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					init_year = year;
					init_month = monthOfYear;
					init_day = dayOfMonth;
					tmp_birthday = String.format("%04d%02d%02d", year, monthOfYear + 1, dayOfMonth);
					birthday.setText(String.format("%04d년 %d월 %d일", year, monthOfYear + 1, dayOfMonth));
				}
			};
    		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				DatePickerDialog datePicker = new DatePickerDialog(JoinActivity.this, onDateSet, init_year, init_month, init_day);
				datePicker.getDatePicker().setCalendarViewShown(false);
				datePicker.show();
			}
		});
    	
    	button_join_submit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				tmp_name		= name.getText().toString();
				tmp_email		= email.getText().toString();
				tmp_pwd			= pwd.getText().toString();
				
//				 Log.d("bigcamp4edu", tmp_name + "/" + tmp_birthday + "/" + tmp_email + "/" + tmp_pwd);
				
				Thread thread = new Thread(new Runnable() {
					public void run() {
						 if(postJoinCheck(JoinActivity))
						 {	// Join Success
							 Bundle extra = new Bundle();
							 Intent intent = new Intent();
							 
							 extra.putString("tmp_email", tmp_email);

							 intent.putExtras(extra);
							 setResult(RESULT_OK, intent);
					
							 finish();
						 }else{ // Join Fail
							 
							 // Show why
							 Toast.makeText(getApplicationContext(), joinFailError, Toast.LENGTH_LONG).show();
						 }
					}
				});
				thread.start();
				thread = null;
				
			}
		});
    	button_join_submit.setEnabled(false);

    }
    private String joinFailError = "";
    
    /*
     * 가입 정보 입력에 따라 '저장' 버튼 활성화/비활성화
     */
    protected void CheckJoinInfo() {
    	String s_name = name.getText().toString();
    	String s_birthday = tmp_birthday;
    	String s_sex = tmp_sex;
    	String s_email = email.getText().toString();
    	String s_pwd = pwd.getText().toString();
    	String s_pwdconfirm = pwdconfirm.getText().toString();
    	
		if( s_name.equals("")
				|| s_name.length() > 32
				|| s_birthday.length() != 8
				|| s_sex.equals("")
				|| !isEmailValid(s_email)
				|| !s_pwd.equals(s_pwdconfirm)
				|| s_pwd.length() < 8
		)
			button_join_submit.setEnabled(false);
		else{
			button_join_submit.setEnabled(true);
		}
	}
    
    /*
     * if this function returns true then your email address is valid, otherwise not
     * (http://stackoverflow.com/questions/9355899/android-email-edittext-validation)
     */
    private boolean isEmailValid(String email)
    {
         String regExpn =
             "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                 +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                   +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                   +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                   +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                   +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

     CharSequence inputStr = email;

     Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
     Matcher matcher = pattern.matcher(inputStr);

     if(matcher.matches())
        return true;
     else
        return false;
}

	/*******************************************************************************
	 * 
	 *	회원가입 동작
	 *
	 ******************************************************************************/
	boolean postJoinCheck(Context context){
		int sex = -1;
		if(tmp_sex.equals("male"))
			sex = 0;
		else if(tmp_sex.equals("female"))
			sex = 1;
		
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();	// 파라메터 값 보내기 위한 배열
    	postParameters.add(new BasicNameValuePair("name",		tmp_name));
    	postParameters.add(new BasicNameValuePair("birthday",	tmp_birthday));
    	postParameters.add(new BasicNameValuePair("sex",	Integer.toString(sex)));
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

//    	    	[Error Code]
//				name length		이름의 글자수가 다릅니다. 32자 이하
//				email pattern	이메일 정규식이 틀렸습니다.
//				exist email		이메일 중복
//				query error		회원가입 insert query 에러

    			String errorCode = res.substring(res.indexOf(errorCodeStart)+errorCodeStart.length(), res.indexOf(errorCodeEnd)).substring(0, 4);
    			if(errorCode.equals("name")){
    				joinFailError = "이름의 글자수가 다릅니다. (32자 이하)";
    			}else if(errorCode.equals("emai")){
    				joinFailError = "이메일 정규식이 틀렸습니다.";
    			}else if(errorCode.equals("exis")){
    				joinFailError = "이메일 중복";
    			}else if(errorCode.equals("quer")){
    				joinFailError = "회원가입 insert query 에러";
    			} 
    			
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
}
