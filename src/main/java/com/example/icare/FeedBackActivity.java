package com.example.icare;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FeedBackActivity extends AppCompatActivity {

    /** Declaring variables*/
    Button feedbut;
    EditText feedtext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

/** Getting Id and assigning them to corresponding variables */
        feedbut = findViewById(R.id.feedbackbut);
        feedtext = findViewById(R.id.feedbacktext);

        /**  Setting an event listener on the feedback button and sending the feedback to the email*/
        feedbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = feedtext.getText().toString();
                if (!text.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Thanks for your feedback!!!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(FeedBackActivity.this, ApplicationSettingsActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Please write something", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}