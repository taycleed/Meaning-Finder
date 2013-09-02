package org.bigcamp4edu.meaningfinder;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

/**
 * Created by TayCleed on 13. 9. 1.
 */
public class HttpClientManager {

    private static HttpClientManager instance;

    private HttpClient httpClient = null;

    public HttpClient getHttpClient()
    {
        if(httpClient != null)
            return httpClient;
        else{
            // Create a new HttpClient and Post Header
            HttpParams params2 = new BasicHttpParams();
            HttpProtocolParams.setVersion(params2, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params2, "UTF-8");

            httpClient = new DefaultHttpClient(params2);
            return httpClient;
        }
    }

    public static HttpClientManager getInstance(){
        if(instance == null)
            instance = new HttpClientManager();

        return instance;
    }
}
