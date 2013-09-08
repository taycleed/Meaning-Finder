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
	 *	����ÿ� Ÿ�Ӿƿ� �ð��� ������
	 *
	 ******************************************************************************/
    public static DefaultHttpClient getHttpClient() {
        if (mHttpClient == null) {
            mHttpClient = new DefaultHttpClient();
            final HttpParams params = mHttpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(params, HTTP_TIMEOUT);		// 10�ʾȿ� �����͸� �������� ���ϸ� exception
            HttpConnectionParams.setSoTimeout(params, HTTP_TIMEOUT);				// 10�ʾȿ� �����͸� �������� ���ϸ� exception
            ConnManagerParams.setTimeout(params, HTTP_TIMEOUT);
        }
        return mHttpClient;
    }
	
	/*******************************************************************************
	 * 
	 *	url�� �������� ������ ��� ��Ʈ������ ����
	 *
	 ******************************************************************************/
    public static String executeHttpPost(String url, ArrayList<NameValuePair> postParameters) throws Exception {
        BufferedReader in = null;
        try {
            UrlEncodedFormEntity	formEntity	= new UrlEncodedFormEntity(postParameters, "UTF_8");	// url-encoded pair ����Ʈ�� �����ϴ� ��ü
        	DefaultHttpClient		client		= getHttpClient();
            HttpPost				request		= new HttpPost(url);						// ��û
            request.setEntity(formEntity);
            HttpResponse			response	= client.execute(request);					// ����
            StringBuffer			sb			= new StringBuffer("");						// ���ڿ� �̾���̱� ���� StringBuffer
            String					line		= "";
            String					NL			= System.getProperty("line.separator");		// ������ ���
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));		// ��û�� ���� ��ü�� ����Ʈ�� �޾ƿͼ� �о����
            
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);														// ���ڿ� �̾���̱�
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