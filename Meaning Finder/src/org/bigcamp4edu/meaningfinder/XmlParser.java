package org.bigcamp4edu.meaningfinder;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.util.Log;

/*----------------------------------------------------------------------------------
*
*	Program	: XML 파서
*	Author	: Jeon Myung Geun
*	Date	: 2013-07-05
*	Path	: /src/com.ggook.addressbook/PhotoViewActivity.java
*	Comment	: 각종 데이터 파싱
*
---------------------------------------------------------------------------------*/
public class XmlParser {
	private static final String TAG = "XMLPARSER";
	private LogoActivity main;
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
        try{
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance(); 
			factory.setNamespaceAware(true);
			XmlPullParser xpp		= factory.newPullParser();
			URL url					= new URL(DB.listUrl+"?ic=100&userId=" + Var.userId); // 실제 xml을 받을수 있는 경로..
			URLConnection uc		= url.openConnection();
			InputStream	in			= uc.getInputStream();
			xpp.setInput(in, "UTF-8");
			int eventType			= xpp.getEventType();
			
		    final String Name			= "name";
		    final String starImg		= "starImg";
		    final String questionNo		= "no";
		    int i = 0;
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
			    	if(xpp.getName().toString().equals(questionNo)){
			    		Var.listReqNo.add(xpp.nextText().toString());
				    }else if(xpp.getName().toString().equals(Name)){
				    	Var.listText.add(xpp.nextText().toString());
				    }else if(xpp.getName().toString().equals(starImg)){
				    	Var.listImgName.add(xpp.nextText().toString());
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
	
	public static boolean getAnswerText(int questionNo){
		try{
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance(); 
			factory.setNamespaceAware(true);
			XmlPullParser xpp		= factory.newPullParser();
			URL url					= new URL(DB.answerINfoUrl+"?questionNo=" + questionNo + "&userId=" + Var.userId); // 실제 xml을 받을수 있는 경로..
			URLConnection uc		= url.openConnection();
			InputStream	in			= uc.getInputStream();
			xpp.setInput(in, "UTF-8");
			int eventType			= xpp.getEventType();
			
//		    final String Name			= "name";
//		    final String starImg		= "starImg";
//		    final String questionNo		= "no";
//		    int i = 0;
//			while (eventType != XmlPullParser.END_DOCUMENT) {					// XML의 끝일때까지 반복
//				switch(eventType){
//				// 문서의 시작
//			    case XmlPullParser.START_DOCUMENT:								
//			    break;
//				
//			    // 문서의 끝
//			    case XmlPullParser.END_DOCUMENT:
//			    break;
//			    
//				// 태그의 시작
//			    case XmlPullParser.START_TAG:									// <? ~~ ?> 인가?
//			    	if(xpp.getName().toString().equals(questionNo)){
//			    		Var.listReqNo.add(xpp.nextText().toString());
//				    }else if(xpp.getName().toString().equals(Name)){
//				    	Var.listText.add(xpp.nextText().toString());
//				    }else if(xpp.getName().toString().equals(starImg)){
//				    	Var.listImgName.add(xpp.nextText().toString());
//				    }
//			    break;
//	
//			    // 태그의 끝
//			    case XmlPullParser.END_TAG:										// < ~~ > 인가?	
//			    break;
//	
//			    // 텍스트
//			    case XmlPullParser.TEXT:
//			    break;
//				}
//				
//				eventType = xpp.next();
//			}
//			
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
}