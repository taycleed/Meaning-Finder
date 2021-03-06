package org.bigcamp4edu.meaningfinder;

import java.util.ArrayList;
import java.util.HashMap;

import org.bigcamp4edu.meaningfinder.util.QuestionListItemType;
import org.bigcamp4edu.meaningfinder.util.StarListItemType;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;

public class Var {
	public static ArrayList<QuestionListItemType> list_questions	= new ArrayList<QuestionListItemType>();
	public static HashMap<String, StarListItemType> list_stars = new HashMap<String, StarListItemType>();
	
	public static String questionNo			= null;
	
	public static String get_star_name		= null;
	public static String get_star_name_en	= null;
	public static String get_star_img		= null;
	public static String get_question_no	= null;
	public static String get_question_name	= null;
	public static String get_answer			= null;
	public static Boolean get_exists_quesion	= false;
	
	public static String insertQuestionNo	= null;
	
	
	
	public static String info_star_name		= null;
	public static String info_star_name_en	= null;
	public static String info_star_img		= null;
	public static String info_question_name	= null;
	public static String info_answer_name	= null;
	
	public static boolean FINISH			= false;								// 종료 시 체크하기 
	public static boolean START				= true;									// 처음 시작인지 체크하기
	
	public static String userPw				= null;									// 사용자의 비밀번호
	public static String userId				= null;									// 사용자의 아이디
	public static String URL				= null;									// 이동할 URL
	
	public static boolean LOGIN_STATE		= false;								// 로그인 여부
	
	public static boolean end_page 			= false;								// endPage인지 여부

	public static ProgressDialog pd;												// 프로그레스 다이얼로그
	
	public static boolean BACKGROUND_APP	= true;									// 앱이 백그라운드에서 도는지 체크
	public static boolean SETTING_STATE		= false;								// 세팅화면을 가는지 체크
	public static boolean LOGIN_PAGE		= false;								// 로그인 페이지에서 메인으로 바로 가는지 체크
	public static boolean WEB_GOBACK		= false;								// 이전 버튼 클릭 시
	
	public static boolean AUTOLOGIN_STATE;											// 자동로그인 상태
	
	public static void InitLoginInfo(Context context){
		SharedPreferences pref 	= context.getSharedPreferences("Setting", 0);
		
		String prefUserId		= pref.getString("userId", "");
		String prefUserPw		= pref.getString("userPw", "");
		Boolean prefLOGIN_STATE	= pref.getBoolean("LOGIN_STATE", false);
		
		if(!prefUserId.equals("") && !prefUserPw.equals("") && prefLOGIN_STATE)
		{
			Var.userId		= (String) prefUserId;
			Var.userPw		= (String) prefUserPw;
			Var.LOGIN_STATE	= (Boolean) prefLOGIN_STATE;
		}
		
		
		if(pref.getString("one_alarm", "none").equals("none")){
			// No alarm has been set/unset. 
			// Set default alarm. 
			SetupActivity.UpdateAlarm(context, 0);
			
			pref.edit()
				.putString("one_alarm", "set")
				.putInt("one_alarm_hour", 7)
				.putInt("one_alarm_minute", 0)
				.putString("two_alarm", "set")
				.putInt("two_alarm_hour", 23)
				.putInt("two_alarm_minute", 0)
				.putBoolean("Vibrate", true)
				.commit();
		}
	}
}
