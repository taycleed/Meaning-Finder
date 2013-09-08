package org.bigcamp4edu.meaningfinder;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class UrlPost {
    public static final int			HTTP_TIMEOUT = 10 * 1000;						// milliseconds
    public static DefaultHttpClient	mHttpClient;

	/*******************************************************************************
	 * 
	 *	연결시에 타임아웃 시간을 정해줌
	 *
	 ******************************************************************************/
    public static DefaultHttpClient getHttpClient() {
        if (mHttpClient == null) {
            mHttpClient = new DefaultHttpClient();
            final HttpParams params = mHttpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(params, HTTP_TIMEOUT);		// 10초안에 데이터를 가져오지 못하면 exception
            HttpConnectionParams.setSoTimeout(params, HTTP_TIMEOUT);				// 10초안에 데이터를 가져오지 못하면 exception
            ConnManagerParams.setTimeout(params, HTTP_TIMEOUT);
        }
        return mHttpClient;
    }
	
	/*******************************************************************************
	 * 
	 *	url에 포스팅할 내용을 담아 스트링으로 리턴
	 *
	 ******************************************************************************/
    public static String executeHttpPost(String url, ArrayList<NameValuePair> postParameters) throws Exception {
        BufferedReader in = null;
        try {
            UrlEncodedFormEntity	formEntity	= new UrlEncodedFormEntity(postParameters, "UTF_8");	// url-encoded pair 리스트를 구성하는 개체
        	DefaultHttpClient		client		= getHttpClient();
            HttpPost				request		= new HttpPost(url);						// 요청
            request.setEntity(formEntity);
            HttpResponse			response	= client.execute(request);					// 응답
            StringBuffer			sb			= new StringBuffer("");						// 문자열 이어붙이기 위한 StringBuffer
            String					line		= "";
            String					NL			= System.getProperty("line.separator");		// 구분자 사용
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));		// 요청을 통해 실체와 컨텐트를 받아와서 읽어들임
            
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);														// 문자열 이어붙이기
            }
            in.close();
            String result = sb.toString();
            return result;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}