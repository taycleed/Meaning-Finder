package org.bigcamp4edu.meaningfinder;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.bigcamp4edu.meaningfinder.util.QuestionListItemType;
import org.bigcamp4edu.meaningfinder.util.StarListItemType;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import android.content.pm.PackageInfo;
import android.util.Log;


public class XmlParser {
//	private static final String TAG = "XMLPARSER";
//	private LogoActivity main;
	static String versionText		= "";
	static PackageInfo packageInfo;
	/*******************************************************************************
	 * 
	 *	앱 버전 체크
	 * @return 
	 *
	 ******************************************************************************/
	@SuppressWarnings("unused")
	public static boolean getListText(){
		Log.d("VOM XmlParser", "getListText() userId=" + Var.userId);
		
		// List 초기화
		Var.list_questions.clear();
		Var.list_stars.clear();
		
        try{
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance(); 
			factory.setNamespaceAware(true);
			XmlPullParser xpp		= factory.newPullParser();
			URL url					= new URL(DB.listUrl+"?ic=100&userId=" + Var.userId); // 실제 xml을 받을 수 있는 경로..
			URLConnection uc		= url.openConnection();
			InputStream	in			= uc.getInputStream();
			xpp.setInput(in, "UTF-8");
			int eventType			= xpp.getEventType();
			
		    final String Name			= "name";
		    final String starName		= "starName";
		    final String starImg		= "starImg";
		    final String questionNo		= "no";
		    final String time		= "time";
		    int i = 0;
		    
		    int reqNo = -1;
		    String qText = "", qName = "", qImgName = "";
		    long timeStamp = 0;
			while (eventType != XmlPullParser.END_DOCUMENT) {					// XML의 끝일때까지 반복
				switch(eventType){
				// 문서의 시작
			    case XmlPullParser.START_DOCUMENT:								
			    break;
				
			    // 문서의 끝
			    case XmlPullParser.END_DOCUMENT:
			    break;
			    
				// 태그의 시작
			    case XmlPullParser.START_TAG:									// <? ~~ ?> 인가?
			    	Log.i("TEXTNAME", xpp.getName().toString());
			    	
			    	if(xpp.getName().toString().equals(questionNo)){
			    		reqNo = Integer.parseInt(xpp.nextText().toString());
				    }else if(xpp.getName().toString().equals(Name)){
				    	qText = xpp.nextText().toString();
				    }else if(xpp.getName().toString().equals(starName)){
				    	qName = xpp.nextText().toString();
				    }else if(xpp.getName().toString().equals(starImg)){
				    	qImgName = xpp.nextText().toString();
					}else if(xpp.getName().toString().equals(time)){
						timeStamp = Long.parseLong(xpp.nextText().toString());
						
						if (reqNo != -1 && !qText.equals("") && !qName.equals("") && !qImgName.equals("") &&  timeStamp != 0) {
							Var.list_questions.add(new QuestionListItemType(reqNo, qText, qName, qImgName, timeStamp));
							
							Log.d("VOM XmlParser", "QuestionListItem Added: " + Integer.toString(reqNo) + ", " + qText + ", " + qName + ", " + qImgName + ", " + Long.toString(timeStamp));
							
							StarListItemType slItem = new StarListItemType(qName, qImgName);
							if(!Var.list_stars.containsKey(slItem.starName))	// 별자리 리스트용 데이터
								Var.list_stars.put(slItem.starName, slItem);
							// 초기화
							reqNo = -1;
							qText = "";
							qName = "";
							qImgName = "";
							timeStamp = 0;
						}
					}
			    break;

			    // 태그의 끝
			    case XmlPullParser.END_TAG:										// < ~~ > 인가?	
			    break;

			    // 텍스트
			    case XmlPullParser.TEXT:
			    break;
				}
				
				eventType = xpp.next();
			}
			
			// 질문을 시간 역순으로 정렬
			Collections.sort(Var.list_questions, new Comparator<QuestionListItemType>() {
				@Override
				public int compare(QuestionListItemType lhs, QuestionListItemType rhs) {
					return -(lhs.listReqNo - rhs.listReqNo);
				}
			});
			
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
    }
	
	public static boolean getAnswerText(){
		Log.d("VOM XmlParser", "getAnswerText() userId=" + Var.userId + " questionNo=" + Var.questionNo );
		
		try{
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance(); 
			factory.setNamespaceAware(true);
			XmlPullParser xpp		= factory.newPullParser();
			URL url					= new URL(DB.answerINfoUrl+"?questionNo=" + Var.questionNo + "&userId=" + Var.userId); // 실제 xml을 받을수 있는 경로..
			URLConnection uc		= url.openConnection();
			InputStream	in			= uc.getInputStream();
			xpp.setInput(in, "UTF-8");
			int eventType			= xpp.getEventType();
			
			final String starName		= "starName";
			final String starNameEn		= "starNameEn";
			final String starImg		= "starImg";
			
			
//			final String qNo			= "questionNo";			
			final String qName			= "questionName";			
			
//			final String ansNo			= "answerNo"; 
			final String ansName		= "answerName"; 

			final String errorCode		= "errorCode";

//		    int i = 0;
			while (eventType != XmlPullParser.END_DOCUMENT) {					// XML의 끝일때까지 반복
				switch(eventType){
				// 문서의 시작
			    case XmlPullParser.START_DOCUMENT:								
			    break;
			    
			    // 문서의 끝
			    case XmlPullParser.END_DOCUMENT:
			    break;
			    
				// 태그의 시작
			    case XmlPullParser.START_TAG:									// <? ~~ ?> 인가?
			    	Log.i("TEXTNAME", xpp.getName().toString());
			    	if(xpp.getName().toString().equals(starName)){
			    		//Var.listReqNo.add(xpp.nextText().toString());
			    		Var.info_star_name		= (String) xpp.nextText().toString();
				    }else if(xpp.getName().toString().equals(starNameEn)){
				    	Var.info_star_name_en	= (String) xpp.nextText().toString();
				    }else if(xpp.getName().toString().equals(starImg)){
				    	Var.info_star_img		= (String) xpp.nextText().toString();
				    }else if(xpp.getName().toString().equals(qName)){
				    	Var.info_question_name	= (String) xpp.nextText().toString();
				    }else if(xpp.getName().toString().equals(ansName)){
				    	Var.info_answer_name	= (String) xpp.nextText().toString();
				    
				    }else if(xpp.getName().toString().equals(errorCode)){
				    	Log.e("VOM XmlParser", "getAnswerText() error code : " + (String) xpp.getText().toString() );
				    }
			    break;
	
			    // 태그의 끝
			    case XmlPullParser.END_TAG:										// < ~~ > 인가?	
			    break;
	
			    // 텍스트
			    case XmlPullParser.TEXT:
			    break;
				}
				
				eventType = xpp.next();
			}
			
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	
	
	public static boolean getQuestion(){
		try{
			Log.d("VOM XmlParser", "getQuestion() userId : " + Var.userId);
			
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance(); 
			factory.setNamespaceAware(true);
			XmlPullParser xpp		= factory.newPullParser();
			URL url					= new URL(DB.getQuestionUrl+"?userId=" + Var.userId); // 실제 xml을 받을수 있는 경로..
			URLConnection uc		= url.openConnection();
			InputStream	in			= uc.getInputStream();
			xpp.setInput(in, "UTF-8");
			int eventType			= xpp.getEventType();
			
			final String starName		= "starName";
			final String starNameEn		= "starNameEn";
			final String starImg		= "starImg";
			
			
			final String qNo			= "questionNo";			
			final String qName			= "questionName";			
			
//			final String ansNo			= "answerNo"; 
//			final String ansName		= "answerName"; 
			
			final String errorCode		= "errorCode";

//		    int i = 0;
			while (eventType != XmlPullParser.END_DOCUMENT) {					// XML의 끝일때까지 반복
				switch(eventType){
				// 문서의 시작
			    case XmlPullParser.START_DOCUMENT:								
			    break;
			    
			    // 문서의 끝
			    case XmlPullParser.END_DOCUMENT:
			    break;
			    
				// 태그의 시작
			    case XmlPullParser.START_TAG:									// <? ~~ ?> 인가?
			    	Log.i("TEXTNAME", xpp.getName().toString());
			    	
			    	if(xpp.getName().toString().equals(starName)){
			    		//Var.listReqNo.add(xpp.nextText().toString());
			    		Var.get_star_name		= (String) xpp.nextText().toString();
						
				    }else if(xpp.getName().toString().equals(starNameEn)){
				    	Var.get_star_name_en	= (String) xpp.nextText().toString();
				    }else if(xpp.getName().toString().equals(starImg)){
				    	Var.get_star_img		= (String) xpp.nextText().toString();
				    	Log.d("VOM XmlParser", "StarImage Name: " + Var.get_star_img);
				    }else if(xpp.getName().toString().equals(qNo)){
				    	Var.get_question_no		= (String) xpp.nextText().toString();
				    }else if(xpp.getName().toString().equals(qName)){
				    	Var.get_question_name	= (String) xpp.nextText().toString();
				    	
				    }else if(xpp.getName().toString().equals(errorCode)){
				    	
				    	Log.e("VOM XmlParser", "getQuestion() error code : " + (String) xpp.getText().toString() );
				    }
			    break;
	
			    // 태그의 끝
			    case XmlPullParser.END_TAG:										// < ~~ > 인가?	
			    break;
	
			    // 텍스트
			    case XmlPullParser.TEXT:
			    break;
				}
				
				eventType = xpp.next();
			}
			
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	
	public static boolean insertAnswer(){
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();	// 파라메터 값 보내기 위한 배열
    	postParameters.add(new BasicNameValuePair("userId",		Var.userId));
    	postParameters.add(new BasicNameValuePair("questionNo",	Var.get_question_no));
    	postParameters.add(new BasicNameValuePair("answer",		Var.get_answer));
    	
    	
    	try {

    	    String response			= UrlPost.executeHttpPost(DB.answerJoinUrl, postParameters);
    	    Log.i("test", response);
    	    String res				= response.toString();
    	    String resultStart		= "<result>";
    	    String resultEnd		= "</result>";
    	    
    	    String questionNoStart		= "<questionNo>";
    	    String questionNoEnd		= "</questionNo>";
    	    
//    	    String errorCodeStart	= "<code>";
//    	    String errorCodeEnd		= "</code>";
    	    String result			= null;
    	    String insertQuestionNo	= null;
    	    
    	    res = res.replaceAll("\\s+", "");
    	    try{
    	    	result 				= res.substring(res.indexOf(resultStart)+resultStart.length(), res.indexOf(resultEnd));
    	    	insertQuestionNo	= res.substring(res.indexOf(questionNoStart)+questionNoStart.length(), res.indexOf(questionNoEnd));
	    	    	Log.i("TOKEN", insertQuestionNo);
    	    }catch(Exception e){
    	    	e.printStackTrace();
    	    }
    	    
    	    if(result.equals("true")){												// 성공 메시지일 경우
    	    	Var.insertQuestionNo	= insertQuestionNo;
    	    	return true;
    	    }else{																	// 성공이 아닐 경우
    			
    	    	return false;
    	    }
    	} catch (Exception e) {
    		e.printStackTrace();
    		return false;
    	}
	}
	
	
	
	
	
}