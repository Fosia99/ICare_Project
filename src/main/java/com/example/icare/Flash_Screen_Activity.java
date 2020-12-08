package com.example.icare;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class Flash_Screen_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash__screen_);


        /**Splash Screen duration*/

        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent home = new Intent(Flash_Screen_Activity.this, LoginActivity.class);
                startActivity(home);
                Flash_Screen_Activity.this.finish();
            }
        }, secondsDelayed * 1500);
    }


}