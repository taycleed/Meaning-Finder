package org.bigcamp4edu.meaningfinder;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import android.content.pm.PackageInfo;
import android.util.Log;


public class XmlParser {
	private static final String TAG = "XMLPARSER";
	private LogoActivity main;
	static String versionText		= "";
	static PackageInfo packageInfo;
	/*******************************************************************************
	 * 
	 *	�� ���� üũ
	 * @return 
	 *
	 ******************************************************************************/
	@SuppressWarnings("unused")
	public static boolean getListText(){
        try{
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance(); 
			factory.setNamespaceAware(true);
			XmlPullParser xpp		= factory.newPullParser();
			URL url					= new URL(DB.listUrl+"?ic=100&userId=" + Var.userId); // ���� xml�� ������ �ִ� ���..
			URLConnection uc		= url.openConnection();
			InputStream	in			= uc.getInputStream();
			xpp.setInput(in, "UTF-8");
			int eventType			= xpp.getEventType();
			
		    final String Name			= "name";
		    final String starImg		= "starImg";
		    final String questionNo		= "no";
		    int i = 0;
			while (eventType != XmlPullParser.END_DOCUMENT) {					// XML�� ���϶����� �ݺ�
				switch(eventType){
				// ������ ����
			    case XmlPullParser.START_DOCUMENT:								
			    break;
				
			    // ������ ��
			    case XmlPullParser.END_DOCUMENT:
			    break;
			    
				// �±��� ����
			    case XmlPullParser.START_TAG:									// <? ~~ ?> �ΰ�?
			    	if(xpp.getName().toString().equals(questionNo)){
			    		Var.listReqNo.add(xpp.nextText().toString());
				    }else if(xpp.getName().toString().equals(Name)){
				    	Var.listText.add(xpp.nextText().toString());
				    }else if(xpp.getName().toString().equals(starImg)){
				    	Var.listImgName.add(xpp.nextText().toString());
				    }
			    break;

			    // �±��� ��
			    case XmlPullParser.END_TAG:										// < ~~ > �ΰ�?	
			    break;

			    // �ؽ�Ʈ
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
	
	public static boolean getAnswerText(){
		try{
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance(); 
			factory.setNamespaceAware(true);
			XmlPullParser xpp		= factory.newPullParser();
			URL url					= new URL(DB.answerINfoUrl+"?questionNo=" + Var.questionNo + "&userId=" + Var.userId); // ���� xml�� ������ �ִ� ���..
			URLConnection uc		= url.openConnection();
			InputStream	in			= uc.getInputStream();
			xpp.setInput(in, "UTF-8");
			int eventType			= xpp.getEventType();
			
			final String starName		= "starName";
			final String starNameEn		= "starNameEn";
			final String starImg		= "starImg";
			
			
			final String qNo			= "questionNo";			
			final String qName			= "questionName";			
			
			final String ansNo			= "answerNo"; 
			final String ansName		= "answerName"; 

//		    int i = 0;
			while (eventType != XmlPullParser.END_DOCUMENT) {					// XML�� ���϶����� �ݺ�
				switch(eventType){
				// ������ ����
			    case XmlPullParser.START_DOCUMENT:								
			    break;
			    
			    // ������ ��
			    case XmlPullParser.END_DOCUMENT:
			    break;
			    
				// �±��� ����
			    case XmlPullParser.START_TAG:									// <? ~~ ?> �ΰ�?
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
				    }
			    break;
	
			    // �±��� ��
			    case XmlPullParser.END_TAG:										// < ~~ > �ΰ�?	
			    break;
	
			    // �ؽ�Ʈ
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
	
	
	
	
}