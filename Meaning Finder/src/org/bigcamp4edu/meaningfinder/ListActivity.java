package org.bigcamp4edu.meaningfinder;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);


        (new QuestionGetterAsync()).execute( "10", "1", "all");


        Button button1 = (Button) this.findViewById(R.id.button_l_setup);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListActivity.this, SetupActivity.class);
                startActivity(intent);
            }
        });

        Button button2 = (Button) this.findViewById(R.id.button_l_star);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListActivity.this, StarActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list, menu);
        return true;
    }


    public class VOMArrayAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position).id;
        }

        @Override
        public long getItemId(int position) {
//            return list.get(position);
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View row;
            row = inflater.inflate(R.layout.listitem_question, parent, false);
            TextView question;
            ImageView star;
            question = (TextView) row.findViewById(R.id.textView_list_question);
            star = (ImageView) row.findViewById(R.id.imageView_list);
            question.setText(list.get(position).question);
//            list.get(position).star
//            star.setImageResource();

            return (row);
        }

        private List<VOMQuestion> list;
        public VOMArrayAdapter(VOMQuestion[] items){

            list = new ArrayList<VOMQuestion>(items.length);
            for(VOMQuestion i : items) {
                boolean add;
                if (list.add(i)) add = true;
                else add = false;
            }
        }
    }

    public class QuestionGetterAsync extends AsyncTask<String, Void, List<VOMQuestion> >{

//        ic         페이지당 질문갯수
//        pg        선택할 페이지
//        type                all = 모든질문, star = 별자리
//        starNo        type == star 일때 별자리 number 값
        @Override
        protected List<VOMQuestion> doInBackground(String... params) {
            String ic = params[0];
            String pg = params[1];
            String type = params[2] ;
            String starNo= "";
            if(params.length > 3)
                starNo = params[3];
            String myurl = getResources().getString(R.string.url_get_questions);

            HttpClient httpclient = HttpClientManager.getInstance().getHttpClient();
            HttpPost httppost = new HttpPost(myurl);

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
                nameValuePairs.add(new BasicNameValuePair("ic", ic));
                nameValuePairs.add(new BasicNameValuePair("pg", pg));
                nameValuePairs.add(new BasicNameValuePair("userNo", "19"));
                nameValuePairs.add(new BasicNameValuePair("type", type));
                if(params.length > 3)
                    nameValuePairs.add(new BasicNameValuePair("starNo", starNo));
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
                Log.d("bigcamp4edu", contentAsString);
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                Log.d("bigcamp4edu", "1111111111");
                parser.setInput(new ByteArrayInputStream(contentAsString.getBytes("UTF-8")), null);
//                parser.nextTag();
//                while(!parser.getName().equals("questionList"))
//                    parser.nextTag();
//

                List<VOMQuestion> list = new ArrayList<VOMQuestion>();
                int eventType = parser.next();
                String no = "" ,name = "" , starName ="",starImg="" ;
                String LIST = "questionlist";
                while(eventType != XmlPullParser.END_DOCUMENT){

                    if(eventType == XmlPullParser.START_TAG){
                        String tagName = parser.getName();

                        if(tagName == null){
                            eventType = parser.next();
                            continue;
                        }else if(tagName.equals("no")){
                            no = parser.nextText();
                        }else if(tagName.equals("name")){
                            name = parser.nextText();
                        }else if(tagName.equals("starName")){
                            starName = parser.nextText();
                        }else if(tagName.equals("starImg")){
                            starImg = parser.nextText();
                        }
                    }else if(eventType == XmlPullParser.END_TAG){
                        String tagName = parser.getName();
                        if(tagName == null){
                            eventType = parser.next();
                            continue;
                        }

                        if(tagName.equals("question")){
                            list.add(new VOMQuestion(name, starImg, Integer.parseInt(no)));
                        }

                        if(tagName.equals(LIST)){
                            break;
                        }
                    }
                    eventType = parser.next();
                }

                return list;

                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                Log.e("bigcamp4edu", e.toString());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                Log.e("bigcamp4edu", e.toString());
            } catch (XmlPullParserException e) {
                Log.e("bigcamp4edu", e.toString());
            }


            return null;
        }

        @Override
        protected void onPostExecute(List<VOMQuestion> vomQuestions) {
            super.onPostExecute(vomQuestions);

            if(vomQuestions == null){

                return ;
            }

            ListView questions = (ListView) findViewById(R.id.listView_questions);
//            VOMQuestion[] vq = new VOMQuestion[]{
//                    new VOMQuestion("Question1", "star1", 1),
//                    new VOMQuestion("Question2", "star2", 2),
//                    new VOMQuestion("Question3", "star3", 3)};

            VOMArrayAdapter listAdapter = new VOMArrayAdapter((VOMQuestion[]) (vomQuestions.toArray()));
            questions.setAdapter(listAdapter);
        }
    }
}
