package com.example.icare;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class TestemoniesActivity extends AppCompatActivity {
    /** Declaring variables*/
    private FirebaseAuth mAuth;
    private String currentUserId;
    private TextView content, name;
    private Button share;
    private ImageView image;
    private Toolbar mToolbar;
    private String Description;
    private DatabaseReference TestimoniesRef, UserRef;
    private RecyclerView TestimoniesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testemonies);

        /** Getting Id and assigning them to corresponding variables */
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        content = (TextView) findViewById(R.id.Edit_content);
        share = (Button) findViewById(R.id.save_testimony);
        name = (TextView) findViewById(R.id.testimony_username);
        image = (ImageView) findViewById(R.id.testimony_profile);

        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        TestimoniesRef = FirebaseDatabase.getInstance().getReference().child("Testimonies");

        /** sets an onclick listener to the share button >> sends to validate info method*/
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateInfo();

            }
        });

    }

    /** Validates that the text fields are not empty*/
    private void ValidateInfo() {

        Description = content.getText().toString();
        if (TextUtils.isEmpty(Description)) {
            Toast.makeText(this, "Please enter a description ", Toast.LENGTH_SHORT).show();
        } else {
            SaveToDatabase();
        }
    }

/**Creates a database ref , saves the testimony info to the database*/
    private void SaveToDatabase() {
        UserRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    String userFullName = snapshot.child("fullname").getValue().toString();
                    String userProfileImage = snapshot.child("profileimage").getValue().toString();

                    Picasso.get().load(userProfileImage).placeholder(R.drawable.profile_icon).into(image);
                    name.setText(userFullName);

                    Calendar calForDate = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                    String saveCurrentDate = currentDate.format(calForDate.getTime());

                    Calendar calForTime = Calendar.getInstance();
                    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
                    String saveCurrentTime = currentTime.format(calForTime.getTime());

                    String RandomName = saveCurrentDate + saveCurrentTime;

                    HashMap postsMap = new HashMap();

                    postsMap.put("uid", currentUserId);
                    postsMap.put("date", saveCurrentDate);
                    postsMap.put("time", saveCurrentTime);
                    postsMap.put("description", Description);
                    postsMap.put("profileimage", userProfileImage);
                    postsMap.put("fullname", userFullName);

                    TestimoniesRef.child(currentUserId + RandomName).updateChildren(postsMap).addOnCompleteListener
                            (new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {

                                Toast.makeText(TestemoniesActivity.this, "New Post is updated successfully", Toast.LENGTH_SHORT).show();
                                Intent Intent = new Intent(TestemoniesActivity.this,TestimonyMainActivity.class);
                                startActivity(Intent);
                                finish();
                            } else {
                                String message = task.getException().getMessage();
                                Toast.makeText(TestemoniesActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
