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
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class Signs_and_symptomsActivity extends AppCompatActivity {

    /** Declaring variables*/
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signs_and_symptoms);

        /** Getting Id and assigning them to corresponding variables */

        mToolbar = (Toolbar) findViewById(R.id.signs_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Signs and Symptoms");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawable_layout1);
        actionBarDrawerToggle = new ActionBarDrawerToggle(Signs_and_symptomsActivity.this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.navigation_signs);
        View navView = navigationView.inflateHeaderView(R.layout.hearder2);

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
        public boolean onOptionsItemSelected (@NonNull MenuItem item){

            if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

    /**  Setting event listener to the items on the  Menu*/
        private void UserMenuSelector (MenuItem item){
            switch (item.getItemId()) {

                case R.id.sign_prevention:
                    SendUserToPreventionActivity();
                    Toast.makeText(this, "Preventative measures", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.sign_treatment:
                    SendUserToTreatmentActivity();
                    Toast.makeText(this, "Treatment measures", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.signs_signs:
                    SendUserToSignsActivity();
                    Toast.makeText(this, "Signs", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.sign_self_exam:
                    SendUserToSelfExamActivity();
                    Toast.makeText(this, "Examination Tips", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.sign_risk_factors:
                    SendUserToRisksActivity();
                    Toast.makeText(this, "Risk Factors", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.sign_more_info:
                    SendUserToMoreInfoActivity();
                    Toast.makeText(this, "More Information", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.sign_get_involved:
                    SendUserToGetInvolvedActivity();
                    Toast.makeText(this, "Get Involved", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.signs_into:
                    SendUserToIntroActivity();
                    Toast.makeText(this, "Get Involved", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    /** Intents to send from one Activity to another */
      private void SendUserToIntroActivity() {
        Intent moreIntent = new Intent(Signs_and_symptomsActivity.this,AboutCancerActivity.class);
        startActivity(moreIntent);
        }
       private void SendUserToMoreInfoActivity() {
            Intent moreIntent = new Intent(Signs_and_symptomsActivity.this,MoreInfoActivity.class);
            startActivity(moreIntent);
        }
        private void SendUserToGetInvolvedActivity() {
            Intent involvedIntent = new Intent(Signs_and_symptomsActivity.this,GetInvolvedActivity.class);
            startActivity(involvedIntent);
        }
        private void SendUserToPreventionActivity() {
            Intent preventionIntent = new Intent(Signs_and_symptomsActivity.this,PreventionActivity.class);
            startActivity(preventionIntent);
        }
        private void SendUserToTreatmentActivity() {
            Intent treatmentIntent = new Intent(Signs_and_symptomsActivity.this,TreatmentActivity.class);
            startActivity(treatmentIntent);
        }
        private void SendUserToRisksActivity() {
            Intent risksIntent = new Intent(Signs_and_symptomsActivity.this,RiskFactorsActivity.class);
            startActivity(risksIntent);
        }
        private void SendUserToSelfExamActivity() {
            Intent selfIntent = new Intent(Signs_and_symptomsActivity.this,SelfExaminationActivity.class);
            startActivity(selfIntent);
        }
        private void SendUserToSignsActivity() {
            Intent signIntent = new Intent(Signs_and_symptomsActivity.this,Signs_and_symptomsActivity.class);
            startActivity(signIntent);
        }
    }

