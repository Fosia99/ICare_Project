package com.example.icare;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.navigation.NavigationView;

public class GetInvolvedActivity extends AppCompatActivity {
    /** Declaring variables*/

private CardView volunteer,job_listing,tastemonies,donations;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_involved);

/** Getting Id and assigning them to corresponding variables */

        volunteer= (CardView)findViewById(R.id.volunteer);
        job_listing= (CardView)findViewById(R.id.job_listing);
        tastemonies= (CardView)findViewById(R.id.testemonies);
        donations= (CardView)findViewById(R.id.donations);

        mToolbar = (Toolbar) findViewById(R.id.get_app_bar);
        setSupportActionBar(mToolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Get Involved ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawable_layout6);
        actionBarDrawerToggle = new ActionBarDrawerToggle(GetInvolvedActivity.this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.navigation_get);
        View navView = navigationView.inflateHeaderView(R.layout.hearder2);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                UserMenuSelector(item);
                return false;
            }
        });

        /** Sets onclick listener and sends to different activities*/
        volunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(GetInvolvedActivity.this,VolunteerHomeActivity.class);
                startActivity(Intent);
            }
        });
        job_listing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(GetInvolvedActivity.this,JobListingHomeActivity.class);
                startActivity(Intent);
            }
        });
        donations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(GetInvolvedActivity.this,DonationsActivity.class);
                startActivity(Intent);
            }
        });
        tastemonies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(GetInvolvedActivity.this,TestimonyMainActivity.class);
                startActivity(Intent);
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**  Setting event listener to the items on the Get Involved Menu*/
    private void UserMenuSelector(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.get_prevention:
                SendUserToPreventionActivity();
                Toast.makeText(this, "Preventative measures", Toast.LENGTH_SHORT).show();
                break;
            case R.id.get_treatment:
                SendUserToTreatmentActivity();
                Toast.makeText(this, "Treatment measures", Toast.LENGTH_SHORT).show();
                break;
            case R.id.get_signs:
                SendUserToSignsActivity();
                Toast.makeText(this, "Signs", Toast.LENGTH_SHORT).show();
                break;
            case R.id.get_self_exam:
                SendUserToSelfExamActivity();
                Toast.makeText(this, "Examination Tips", Toast.LENGTH_SHORT).show();
                break;
            case R.id.get_risk_factors:
                SendUserToRisksActivity();
                Toast.makeText(this, "Risk Factors", Toast.LENGTH_SHORT).show();
                break;
            case R.id.get_more_info:
                SendUserToMoreInfoActivity();
                Toast.makeText(this, "More Information", Toast.LENGTH_SHORT).show();
                break;
            case R.id.get_get_involved:
                SendUserToGetInvolvedActivity();
                Toast.makeText(this, "Get Involved", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /** Adding animation to the cards*/
    protected void onStart() {
        super.onStart();

        YoYo.with(Techniques.Bounce)
                .duration(1000)
                .repeat(100)
                .playOn(job_listing);

        YoYo.with(Techniques.Bounce)
                .duration(1000)
                .repeat(100)
                .playOn(volunteer);

        YoYo.with(Techniques.Bounce)
                .duration(1000)
                .repeat(100)
                .playOn(donations);

        YoYo.with(Techniques.Bounce)
                .duration(1000)
                .repeat(100)
                .playOn(tastemonies);
    }

    /** Intents to send from one Activity to another */
    private void SendUserToMoreInfoActivity() {
        Intent moreIntent = new Intent(GetInvolvedActivity.this,MoreInfoActivity.class);
        startActivity(moreIntent);
    }

    private void SendUserToGetInvolvedActivity() {
        Intent involvedIntent = new Intent(GetInvolvedActivity.this,GetInvolvedActivity.class);
        startActivity(involvedIntent);
    }

    private void SendUserToPreventionActivity() {
        Intent preventionIntent = new Intent(GetInvolvedActivity.this,PreventionActivity.class);
        startActivity(preventionIntent);
    }

    private void SendUserToTreatmentActivity() {
        Intent treatmentIntent = new Intent(GetInvolvedActivity.this,TreatmentActivity.class);
        startActivity(treatmentIntent);
    }

    private void SendUserToRisksActivity() {
        Intent risksIntent = new Intent(GetInvolvedActivity.this,RiskFactorsActivity.class);
        startActivity(risksIntent);
    }
    private void SendUserToSelfExamActivity() {
        Intent selfIntent = new Intent(GetInvolvedActivity.this,SelfExaminationActivity.class);
        startActivity(selfIntent);
    }
    private void SendUserToSignsActivity() {
        Intent signIntent = new Intent(GetInvolvedActivity.this,Signs_and_symptomsActivity.class);
        startActivity(signIntent);
    }

}