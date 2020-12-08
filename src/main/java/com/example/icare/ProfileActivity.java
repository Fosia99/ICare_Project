package com.example.icare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    /** Declaring variables*/

    private FirebaseAuth mAuth;
    private String currentUserId;
    private ProgressDialog loadingBar;
    final static int Gallery_Pick = 1;
    private StorageReference UserProfileImageRef;
    private FloatingActionButton editProfile;
    private CircleImageView profileImage;
    private DatabaseReference ProfileUserRef;
    private Toolbar mToolbar;
    private TextView profileStatus,profileName,profileUserName,profileDob,profileGender,
            profilePhoneNumber,profileType,profileRelationshipStatus,profileCountry,profileInterests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        /** Getting Id and assigning them to corresponding variables */
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        ProfileUserRef= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        UserProfileImageRef= FirebaseStorage.getInstance().getReference().child("Profile Images");

        mToolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        setSupportActionBar(mToolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("My Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        profileStatus = (TextView) findViewById(R.id.profile_status) ;
        profileName= (TextView) findViewById(R.id.profile_Name) ;
        profileUserName= (TextView) findViewById(R.id.profile_userName) ;
        profileDob= (TextView) findViewById(R.id.profile_dob) ;
        profileGender= (TextView) findViewById(R.id.profile_gender) ;
        profilePhoneNumber= (TextView) findViewById(R.id.profile_phone_number) ;
        profileType= (TextView) findViewById(R.id.profile_User_type) ;
        profileRelationshipStatus= (TextView) findViewById(R.id.profile_relationship_status) ;
        profileCountry= (TextView) findViewById(R.id.profile_country) ;
        profileInterests= (TextView) findViewById(R.id.profile_interests) ;
        profileImage = (CircleImageView) findViewById(R.id.profile_Image);
        editProfile =(FloatingActionButton) findViewById(R.id.edit_my_profile);


        /**Sets an onclick listener to the edit profile button and sends it the Setting Activity*/
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ediIntent = new Intent(ProfileActivity.this, SettingsActivity.class);
                startActivity(ediIntent);
                    }
                });
/**Reads all info about the logged in user from the database and display it*/
        ProfileUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    String myProImage = snapshot.child("profileimage").getValue().toString();
                    Picasso.get().load(myProImage).placeholder(R.drawable.profile).into(profileImage);
                    String myUserName = snapshot.child("username").getValue().toString();
                    profileUserName.setText(myUserName);

                    String myFullName = snapshot.child("fullname").getValue().toString();
                    profileName.setText(myFullName);

                    String myUserProStatus = snapshot.child("profilestatus").getValue().toString();
                    profileStatus.setText(myUserProStatus);

                    String myUserDob = snapshot.child("dateofbirth").getValue().toString();
                    profileDob.setText(myUserDob);

                    String myUserGender = snapshot.child("gender").getValue().toString();
                    profileGender.setText(myUserGender);

                    String myUserPhoneNumber = snapshot.child("phonenumber").getValue().toString();
                    profilePhoneNumber.setText(myUserPhoneNumber);

                    String myUserType = snapshot.child("usertype").getValue().toString();
                    profileType.setText(myUserType);

                    String myUserRelationshipStatus = snapshot.child("relationshipstatus").getValue().toString();
                    profileRelationshipStatus.setText(myUserRelationshipStatus);

                    String myUserCountry = snapshot.child("country").getValue().toString();
                    profileCountry.setText(myUserCountry);

                    String myUserInterests = snapshot.child("interest").getValue().toString();
                    profileInterests.setText(myUserInterests);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}