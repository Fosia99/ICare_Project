package com.example.icare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DonationsActivity extends AppCompatActivity {
    /** Declaring variables*/
      private Button Donationlink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donations);

      /** Getting Id and assigning them to corresponding variables */
        Donationlink = (Button) findViewById(R.id.donate_link);


        /**  Setting event listener to the button and sends to Cancer Association Web page */
        Donationlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.can.org.na/?page_id=1346"));
                startActivity(webIntent);
            }
        });
    }
}