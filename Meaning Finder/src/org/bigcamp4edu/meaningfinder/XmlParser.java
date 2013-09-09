package org.bigcamp4edu.meaningfinder;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.util.Log;

/*----------------------------------------------------------------------------------
*
*	Program	: XML �ļ�
*	Author	: Jeon Myung Geun
*	Date	: 2013-07-05
*	Path	: /src/com.ggook.addressbook/PhotoViewActivity.java
*	Comment	: ���� ������ �Ľ�
*
---------------------------------------------------------------------------------*/
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
        	Log.i("TEXTDATA", "start parser");
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance(); 
			factory.setNamespaceAware(true);
			XmlPullParser xpp		= factory.newPullParser();
			URL url					= new URL(DB.listUrl+"?ic=10&userNo=19"); // ���� xml�� ������ �ִ� ���..
			URLConnection uc		= url.openConnection();
			InputStream	in			= uc.getInputStream();
			xpp.setInput(in, "UTF-8");
			int eventType			= xpp.getEventType();
			
		    final String Name		= "name";
		    final String starImg			= "starImg";
		    int i = 0;
			while (eventType != XmlPullParser.END_DOCUMENT) {					// XML�� ���϶����� �ݺ�
				Log.i("TEXTDATA", "in while");
				switch(eventType){
				// ������ ����
			    case XmlPullParser.START_DOCUMENT:								
			    break;
				
			    // ������ ��
			    case XmlPullParser.END_DOCUMENT:
			    break;
			    
				// �±��� ����
			    case XmlPullParser.START_TAG:									// <? ~~ ?> �ΰ�?
			    	Log.i("TEXTDATA", "in start tag");
			    	if(xpp.getName().toString().equals(Name)){
				    	Var.listText.add(xpp.nextText().toString());
				    	Log.i("TEXTDATA", Var.listText.get(i));
				    }else if(xpp.getName().toString().equals(starImg)){
				    	Var.listImgUrl.add(xpp.nextText().toString());
				    	Log.i("TEXTDATA", Var.listImgUrl.get(i++));
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