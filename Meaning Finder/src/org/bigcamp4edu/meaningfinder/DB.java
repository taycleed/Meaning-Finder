package org.bigcamp4edu.meaningfinder;

public class DB {
	// 다이얼 로그 값 
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
	// 앱 내부 web 주소
	
	
	// 앱 공통 주소
	public final static String loginChkUrl		= "http://mean1.cloudapp.net/api/api_userLogin.php";						// 로그인 체크
	public final static String joinChkUrl		= "http://mean1.cloudapp.net/api/api_userJoinProc.php";						// 회원가입
	public final static String listUrl			= "http://mean1.cloudapp.net/api/_api_requestionList.php";					// 질문 리스트
	public final static String answerINfoUrl	= "http://mean1.cloudapp.net/api/api_requestionInfo.php";					// 답변내용 상세페이지
	public final static String getQuestionUrl	= "http://mean1.cloudapp.net/api/api_getQuestion.php";						// 질문 얻어오기
	public final static String answerJoinUrl	= "http://mean1.cloudapp.net/api/api_insert_answer.php";					// 답변 작성하기
	


	// 옵션 정보 저장할 때 key 값
	public final static String AUTOLOGIN	= "autologin";															// 자동로그인체크
	public final static String USER_ID		= "user_id";															// 아이디
	public final static String USER_PW		= "user_pw";															// 패스워드
	public final static String LAST_ANSWER_DATE = "last_answer_date";												// 마지막으로 질문에 답한 날/시각
}
