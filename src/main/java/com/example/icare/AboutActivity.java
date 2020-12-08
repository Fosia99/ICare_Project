package com.example.icare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class AboutActivity extends AppCompatActivity {
    /**
     * Declaring variables
     */

    private ImageView facebook, twitter, email;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

/** Getting Id and assigning them to corresponding variables */

        mToolbar = (Toolbar) findViewById(R.id.about_ICare);
        setSupportActionBar(mToolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("About ICare");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        facebook = (ImageView) findViewById(R.id.facebook_button);
        twitter = (ImageView) findViewById(R.id.twitter_button);
        email = (ImageView) findViewById(R.id.email);

/**  Setting event listener to layouts and buttons */

/** sends the user to Icare's facebook page */
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fbIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/ICare-104848218120553"));
                startActivity(fbIntent);
            }
        });
/** sends the user to Icare's twitter page */
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent twitterIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:fosiahshavuka@gmail.com"));
                startActivity(twitterIntent);

            }
        });
/** sends the user to Icare's  email address */
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:fosiahshavuka@gmail.com"));
                startActivity(emailIntent);
            }
        });

    }

    /**
     * Adding animation to the images
     */
    protected void onStart() {
        super.onStart();

        YoYo.with(Techniques.Bounce)
                .duration(1000)
                .repeat(10)
                .playOn(email);

        YoYo.with(Techniques.Bounce)
                .duration(1000)
                .repeat(10)
                .playOn(facebook);

        YoYo.with(Techniques.Bounce)
                .duration(1000)
                .repeat(10)
                .playOn(twitter);
    }
}