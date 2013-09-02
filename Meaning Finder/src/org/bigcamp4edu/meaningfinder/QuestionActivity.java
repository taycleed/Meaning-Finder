package org.bigcamp4edu.meaningfinder;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class QuestionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        // set today's question
        TextView tv = (TextView) this.findViewById(R.id.textview_question);
        tv.setText("[Today's Question]");

        Button button = (Button) this.findViewById(R.id.button_question_submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: send inputted text to server

                // Activity change.
                Intent intent = new Intent(QuestionActivity.this, ConfirmActivity.class);
                startActivity(intent);

            }
        });
    }

//    public class QuestionGetterAsync extends AsyncTask<>


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.question, menu);
        return true;
    }
    
}
