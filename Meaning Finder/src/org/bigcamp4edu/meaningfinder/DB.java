package org.bigcamp4edu.meaningfinder;

public class DB {
	// ���̾� �α� �� 
	public final static int NETWORK_CHECK	= 1;
	public final static int LOGIN_CONFIRM	= 2;
	public final static int LOGIN_CHECK		= 3;
	public final static int LOGIN_ERROR		= 4;
	public final static int CALL_TRUE		= 5;
	public final static int CALL_FALSE		= 6;
	public final static int SEARCH_TEXT		= 7;
	public final static int APP_UPDATE		= 8;
	public final static int NO_SEARCH_DATA  = 9;
	
	
	public final static int ADD_SUCCESS		= 1;
	public final static int ADD_FAIL_MSG	= 2;
	public final static int ADD_FAIL		= 3;
	public final static int NOT_LOGINED		= 4;
	// �� ���� web �ּ�
	
	
	// �� ���� �ּ�
	public final static String loginChkUrl		= "http://mean1.cloudapp.net/api/api_userLogin.php";						// �α��� üũ
	public final static String joinChkUrl		= "http://mean1.cloudapp.net/api/api_userJoinProc.php";						// ȸ������
	public final static String listUrl			= "http://mean1.cloudapp.net/api/_api_requestionList.php";					// ���� ����Ʈ
	public final static String answerINfoUrl	= "http://mean1.cloudapp.net/api/api_requestionInfo.php";					// �亯���� ��������
	public final static String getQuestionUrl	= "http://mean1.cloudapp.net/api/api_getQuestion.php";						// ���� ������
	public final static String answerJoinUrl	= "http://mean1.cloudapp.net/api/api_insert_answer.php";					// �亯 �ۼ��ϱ�
	


	// �ɼ� ���� ������ �� key ��
	public final static String AUTOLOGIN	= "autologin";															// �ڵ��α���üũ
	public final static String USER_ID		= "user_id";															// ���̵�
	public final static String USER_PW		= "user_pw";															// �н�����
	public final static String LAST_ANSWER_DATE = "last_answer_date";												// ���������� ������ ���� ��/�ð�
}
