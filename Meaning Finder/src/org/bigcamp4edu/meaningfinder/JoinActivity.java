package org.bigcamp4edu.meaningfinder;

import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JoinActivity extends Activity {

    public class JoinAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String myurl = params[0];

            HttpClient httpclient = HttpClientManager.getInstance().getHttpClient();
            HttpPost httppost = new HttpPost(myurl);

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
                nameValuePairs.add(new BasicNameValuePair("name", params[1]));
                nameValuePairs.add(new BasicNameValuePair("birthday", params[2]));
                nameValuePairs.add(new BasicNameValuePair("email", params[3]));
                nameValuePairs.add(new BasicNameValuePair("pass", params[4]));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

                HttpResponse response = httpclient.execute(httppost);
                Log.d("bigcamp4edu", "The response is: " + response);

                HttpEntity httpEntity = response.getEntity();
                String contentAsString = "";
                if(httpEntity != null){
                    contentAsString = EntityUtils.toString(httpEntity);
                }
                return contentAsString;

                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }

            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("bigcamp4edu", "The real response is: " + s);
            InputStream is = null;
            try {
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                is = new ByteArrayInputStream(s.getBytes("UTF-8"));
                parser.setInput(is, null);
                parser.nextTag();
                while(!parser.getName().equals("result"))
                    parser.nextTag();
                String result = parser.nextText();
                if(result.equals("true")){
                    Toast.makeText(JoinActivity.this, getResources().getText(R.string.join_success), Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(JoinActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    startActivity(intent);

                    finish();

                }else{
                    String errorCode = parser.nextText();

                    if(errorCode.equals("name length")){
                        Toast.makeText(JoinActivity.this, getResources().getText(R.string.join_longname), Toast.LENGTH_LONG).show();
                    }else if(errorCode.equals("email pattern")){
                        Toast.makeText(JoinActivity.this, getResources().getText(R.string.join_wrong_email), Toast.LENGTH_LONG).show();
                    }else if(errorCode.equals("exist email")){
                        Toast.makeText(JoinActivity.this, getResources().getText(R.string.join_email_dup), Toast.LENGTH_LONG).show();
                    }else if(errorCode.equals("query error")){
                        Toast.makeText(JoinActivity.this, getResources().getText(R.string.join_query_error), Toast.LENGTH_LONG).show();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean isSubmittable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        isSubmittable = false;

        Button button = (Button) this.findViewById(R.id.button_join_submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: implement joining process.

                if(!JoinActivity.this.isSubmittable){
                    Toast.makeText(JoinActivity.this, "뭔가 틀렸어요!", Toast.LENGTH_LONG);
                    return ;
                }

                String name, birthday, email, password;
                EditText temp;
                temp = (EditText) JoinActivity.this.findViewById(R.id.editText_name);
                name = temp.getText().toString();
                temp = (EditText) JoinActivity.this.findViewById(R.id.editText_birthday);
                birthday = temp.getText().toString();
                temp = (EditText) JoinActivity.this.findViewById(R.id.editText_email);
                email = temp.getText().toString();
                temp = (EditText) JoinActivity.this.findViewById(R.id.editText_password);
                password = temp.getText().toString();

                Log.d("bigcamp4edu", name + "/" + birthday + "/" + email + "/" + password);

                (new JoinAsyncTask()).execute(getResources().getString(R.string.url_join),
                        name, birthday, email, password);

            }
        });

        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                Button button = (Button) JoinActivity.this.findViewById(R.id.button_join_submit);

                String name, birthday, email, password, passwordagain;
                EditText temp;
                temp = (EditText) JoinActivity.this.findViewById(R.id.editText_name);
                name = temp.getText().toString();

                temp = (EditText) JoinActivity.this.findViewById(R.id.editText_birthday);
                birthday = temp.getText().toString();

                temp = (EditText) JoinActivity.this.findViewById(R.id.editText_email);
                email = temp.getText().toString();

                temp = (EditText) JoinActivity.this.findViewById(R.id.editText_password);
                password = temp.getText().toString();

                temp = (EditText) JoinActivity.this.findViewById(R.id.editText_passwordagain);
                passwordagain = temp.getText().toString();

                if(name.equals("") || birthday.equals("") || email.equals("") || password.equals("") || passwordagain.equals("")
                    || !password.equals(passwordagain) || !email.contains("@") ){
                    if(Build.VERSION.SDK_INT >= 11)
                        button.setAlpha(0.4f);
                    JoinActivity.this.isSubmittable = false;
                }else{
                    if(Build.VERSION.SDK_INT >= 11)
                        button.setAlpha(1f);
                    JoinActivity.this.isSubmittable = true;
                }
            }
        };
        EditText editText_name = (EditText) this.findViewById(R.id.editText_name);
        editText_name.addTextChangedListener(tw);
        editText_name = (EditText) this.findViewById(R.id.editText_email);
        editText_name.addTextChangedListener(tw);
        editText_name = (EditText) this.findViewById(R.id.editText_birthday);
        editText_name.addTextChangedListener(tw);
        editText_name = (EditText) this.findViewById(R.id.editText_password);
        editText_name.addTextChangedListener(tw);
        editText_name = (EditText) this.findViewById(R.id.editText_passwordagain);
        editText_name.addTextChangedListener(tw);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.join, menu);
        return true;
    }
    
}
