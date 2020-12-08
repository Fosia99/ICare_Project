package com.example.icare;


import android.content.Intent;
import android.net.Uri;
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
public class MoreInfoActivity extends AppCompatActivity {
    /**
     * Declaring variables
     */
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar mToolbar;
    private CardView Can, Who;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);

        /** Getting Id and assigning them to corresponding variables */

        mToolbar = (Toolbar) findViewById(R.id.info_app_bar);
        Can = (CardView) findViewById(R.id.can);
        Who = (CardView) findViewById(R.id.who);
        setSupportActionBar(mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("More Information");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        drawerLayout = (DrawerLayout) findViewById(R.id.drawable_layout7);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MoreInfoActivity.this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.navigation_info);
        View navView = navigationView.inflateHeaderView(R.layout.hearder2);


        Can.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent CanIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.can.org.na/?page_id=754"));
                startActivity(CanIntent);
            }
        });

        Who.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent WhoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.who.int/cancer/detection/breastcancer/en/"));
                startActivity(WhoIntent);
            }
        });
/** Sets an Item selected listener the navigation veiw and sends it to the UserMenuSelected method*/
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                UserMenuSelector(item);
                return false;
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

    /**
     * Setting event listener to the items on the More Info Menu
     */
    private void UserMenuSelector(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.info_prevention:
                SendUserToPreventionActivity();
                Toast.makeText(this, "Preventative measures", Toast.LENGTH_SHORT).show();
                break;
            case R.id.info_into:
                SendUserToIntroActivity();
                Toast.makeText(this, "Preventative measures", Toast.LENGTH_SHORT).show();
                break;
            case R.id.info_treatment:
                SendUserToTreatmentActivity();
                Toast.makeText(this, "Treatment measures", Toast.LENGTH_SHORT).show();
                break;
            case R.id.info_signs:
                SendUserToSignsActivity();
                Toast.makeText(this, "Signs", Toast.LENGTH_SHORT).show();
                break;
            case R.id.info_self_exam:
                SendUserToSelfExamActivity();
                Toast.makeText(this, "Examination Tips", Toast.LENGTH_SHORT).show();
                break;
            case R.id.info_risk_factors:
                SendUserToRisksActivity();
                Toast.makeText(this, "Risk Factors", Toast.LENGTH_SHORT).show();
                break;
            case R.id.info_more_info:
                SendUserToMoreInfoActivity();
                Toast.makeText(this, "More Information", Toast.LENGTH_SHORT).show();
                break;
            case R.id.info_get_involved:
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
                .playOn(Can);

        YoYo.with(Techniques.Bounce)
                .duration(1000)
                .repeat(100)
                .playOn(Who);

    }


    /** Intents to send from one Activity to another */
    private void SendUserToIntroActivity() {
        Intent moreIntent = new Intent(MoreInfoActivity.this,AboutCancerActivity.class);
        startActivity(moreIntent);
    }

    private void SendUserToMoreInfoActivity() {
        Intent moreIntent = new Intent(MoreInfoActivity.this,MoreInfoActivity.class);
        startActivity(moreIntent);
    }
    private void SendUserToGetInvolvedActivity() {
        Intent involvedIntent = new Intent(MoreInfoActivity.this,GetInvolvedActivity.class);
        startActivity(involvedIntent);
    }
    private void SendUserToPreventionActivity() {
        Intent preventionIntent = new Intent(MoreInfoActivity.this,PreventionActivity.class);
        startActivity(preventionIntent);
    }
    private void SendUserToTreatmentActivity() {
        Intent treatmentIntent = new Intent(MoreInfoActivity.this,TreatmentActivity.class);
        startActivity(treatmentIntent);
    }
    private void SendUserToRisksActivity() {
        Intent risksIntent = new Intent(MoreInfoActivity.this,RiskFactorsActivity.class);
        startActivity(risksIntent);
    }
    private void SendUserToSelfExamActivity() {
        Intent selfIntent = new Intent(MoreInfoActivity.this,SelfExaminationActivity.class);
        startActivity(selfIntent);
    }
    private void SendUserToSignsActivity() {
        Intent signIntent = new Intent(MoreInfoActivity.this,Signs_and_symptomsActivity.class);
        startActivity(signIntent);

    }


}