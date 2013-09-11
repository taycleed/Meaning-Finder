package org.bigcamp4edu.meaningfinder;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.widget.LinearLayout;

public class Var {
	public static ArrayList<String> listReqNo 	= new ArrayList<String>();
	public static ArrayList<String> listText 	= new ArrayList<String>();
	public static ArrayList<String> listImgName = new ArrayList<String>();
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
}
