package com.example.icare;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ApplicationSettingsActivity extends AppCompatActivity {

    /** Declaring variables*/
    private Switch  fullSwitch, notisoundSwitch, vibSwitch, notishowSwitch;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_settings);

        /** Getting Id and assigning them to corresponding variables */
        fullSwitch = findViewById(R.id.fullswitch);
        notisoundSwitch = findViewById(R.id.notisoundswitch);
        vibSwitch = findViewById(R.id.vibswitch);
        notishowSwitch = findViewById(R.id.notishowswitch);

        FirebaseUser curuser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = curuser.getUid();
        db = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Settings");


        /**  Setting event listener to the Database Reference  to get the stored application settings  for the logged in user */

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childsnap : dataSnapshot.getChildren()) {
                    String key = childsnap.getKey();
                    String value = (String) childsnap.getValue();
                    //Log.d("Settingscheck",key+ " "+value);
                    if (key.equals("Vibration")) {
                        if (value.equals("True")) {
                            vibSwitch.setChecked(true);
                        } else {
                            vibSwitch.setChecked(false);
                        }
                    } else if (key.equals("Sound")) {
                        if (value.equals("True")) {
                            notisoundSwitch.setChecked(true);
                        } else {
                            notisoundSwitch.setChecked(false);
                        }
                    } else if (key.equals("Fullscreen")) {
                        if (value.equals("True")) {
                            fullSwitch.setChecked(true);
                        } else {
                            fullSwitch.setChecked(false);
                        }
                    } else if (key.equals("Notification")) {
                        if (value.equals("True")) {
                            notishowSwitch.setChecked(true);
                        } else {
                            notishowSwitch.setChecked(false);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ;


/**  Setting event listener to the Full Screen Setting Switch and setting the screen to fullscreen if its checked */
        fullSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                togglefullscreen();
            }
        });
/**  Setting event listener to the Notification Sound Setting Switch and allowing notification sounds if its checked */
        notisoundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                togglesound();
            }
        });
/**  Setting event listener to the Vibration Setting Switch and allows the phone to vibrate for notifications  if its checked */
        vibSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                togglevib();
            }
        });
/**  Setting event listener to the Notification Setting Switch and allows the app to show notifications if its checked */
        notishowSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleshow();
            }
        });
    }
    /** Method controlling the apps vibrations*/
    private void togglevib() {
        FirebaseUser curuser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = curuser.getUid();
        db = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Settings").child("Vibration");
        if (vibSwitch.isChecked()) {
            db.setValue("True");
        } else {
            db.setValue("False");
        }
    }
    /** Method controlling the apps sound*/
    private void togglesound() {
        FirebaseUser curuser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = curuser.getUid();
        db = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Settings").child("Sound");
        if (notisoundSwitch.isChecked()) {
            db.setValue("True");
        } else {
            db.setValue("False");
        }
    }
    /** Method controlling the apps 's full screen mode*/
    private void togglefullscreen() {
        FirebaseUser curuser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = curuser.getUid();
        db = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Settings").child("Fullscreen");
        if (fullSwitch.isChecked()) {
            db.setValue("True");
        } else {
            db.setValue("False");
        }
    }
    /** Method controlling the app to show notifications*/
    private void toggleshow() {
        FirebaseUser curuser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = curuser.getUid();
        db = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Settings").child("Notification");
        if (notishowSwitch.isChecked()) {
            db.setValue("True");
        } else {
            db.setValue("False");
        }
    }



    public void onDef(View v) {
        resetDefault();
    }

    /** Method that sets the applications  settings to default*/

    private void resetDefault() {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(ApplicationSettingsActivity.this);
        dialog.setTitle("Reset");
        dialog.setMessage("Do you want to reset settings of default?");
        dialog.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseUser curuser = FirebaseAuth.getInstance().getCurrentUser();
                String uid = curuser.getUid();
                db = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Settings").child("Vibration");
                db.setValue("True");
                db = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Settings").child("Sound");
                db.setValue("True");
                db = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Settings").child("Fullscreen");
                db.setValue("False");
                db = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Settings").child("Notification");
                db.setValue("True");
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }
    /** Intents to send from one Activity to another */
    public void onProfile(View v) {
        Intent intent = new Intent(ApplicationSettingsActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    public void onFeed(View v) {
        Intent intent = new Intent(ApplicationSettingsActivity.this, FeedBackActivity.class);
        startActivity(intent);
    }
}
