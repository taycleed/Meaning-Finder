package org.bigcamp4edu.meaningfinder;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.widget.LinearLayout;

public class Var {
	public static ArrayList<String> listReqNo 	= new ArrayList<String>();
	public static ArrayList<String> listText 	= new ArrayList<String>();
	public static ArrayList<String> listImgName = new ArrayList<String>();
	public static boolean FINISH			= false;								// ���� �� üũ�ϱ� 
	public static boolean START				= true;									// ó�� �������� üũ�ϱ�
	
	public static String userPw				= null;									// ������� ��й�ȣ
	public static String userId				= null;									// ������� ���̵�
	public static String URL				= null;									// �̵��� URL
	
	public static boolean LOGIN_STATE		= false;								// �α��� ����
	
	public static boolean end_page 			= false;								// endPage���� ����

	public static ProgressDialog pd;												// ���α׷��� ���̾�α�
	
	public static boolean BACKGROUND_APP	= true;									// ���� ��׶��忡�� ������ üũ
	public static boolean SETTING_STATE		= false;								// ����ȭ���� ������ üũ
	public static boolean LOGIN_PAGE		= false;								// �α��� ���������� �������� �ٷ� ������ üũ
	public static boolean WEB_GOBACK		= false;								// ���� ��ư Ŭ�� ��
	
	public static boolean AUTOLOGIN_STATE;											// �ڵ��α��� ����
}
