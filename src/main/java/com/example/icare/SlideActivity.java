package com.example.icare;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class SlideActivity extends AppCompatActivity {
    /** Declaring variables*/
    private ViewPager vp;
    private SliderAdapter sada;
    private Button gobut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);

        /** Getting Id and assigning them to corresponding variables */

        vp = (ViewPager) findViewById(R.id.slidevp);
        sada = new SliderAdapter();
        vp.setAdapter(sada);
        }
}