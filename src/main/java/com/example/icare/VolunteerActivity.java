  package com.example.icare;

  import android.content.Intent;
  import android.os.Bundle;
  import android.text.TextUtils;
  import android.view.View;
  import android.widget.Button;
  import android.widget.EditText;
  import android.widget.ImageView;
  import android.widget.Toast;

  import androidx.annotation.NonNull;
  import androidx.appcompat.app.AppCompatActivity;
  import androidx.appcompat.widget.Toolbar;
  import androidx.recyclerview.widget.RecyclerView;

  import com.google.android.gms.tasks.OnCompleteListener;
  import com.google.android.gms.tasks.Task;
  import com.google.firebase.auth.FirebaseAuth;
  import com.google.firebase.database.DatabaseReference;
  import com.google.firebase.database.FirebaseDatabase;

  import java.text.SimpleDateFormat;
  import java.util.Calendar;
  import java.util.HashMap;

  public class VolunteerActivity extends AppCompatActivity {
      /** Declaring variables*/
      private FirebaseAuth mAuth;
      private String currentUserId;
      private EditText content, title,date,location,time,contact;
      private Button share;
      private ImageView image;
      private Toolbar mToolbar;
      private String Description,Time,Title,Contact,Date,Location;
      private DatabaseReference VolunteersRef, UserRef;
      private RecyclerView VolunteerList;

      @Override
      protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_volunteer);
          /** Getting Id and assigning them to corresponding variables */

          mAuth = FirebaseAuth.getInstance();
          currentUserId = mAuth.getCurrentUser().getUid();
          content = (EditText) findViewById(R.id.volunteer_content);
          time= (EditText) findViewById(R.id.volnteer_time);
          title = (EditText) findViewById(R.id.volunteer_title);
          location= (EditText) findViewById(R.id.volnteer_location);
          contact = (EditText) findViewById(R.id.volunteer_contact);
          date= (EditText) findViewById(R.id.volnteer_date);

          share = (Button) findViewById(R.id.save_volunteer);
          UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
          VolunteersRef = FirebaseDatabase.getInstance().getReference().child("Volunteer");

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
          Time = time.getText().toString();
          Title = title.getText().toString();
          Date = date.getText().toString();
          Contact = contact.getText().toString();
          Location = location.getText().toString();

          if (TextUtils.isEmpty(Description)) {
              Toast.makeText(this, "Please enter a description ", Toast.LENGTH_SHORT).show(); }
          else  if (TextUtils.isEmpty(Time)) {
              Toast.makeText(this, "Please enter a time", Toast.LENGTH_SHORT).show(); }
          else   if (TextUtils.isEmpty(Title)) {
              Toast.makeText(this, "Please enter a title ", Toast.LENGTH_SHORT).show(); }
          else  if (TextUtils.isEmpty(Contact)) {
              Toast.makeText(this, "Please enter a contact ", Toast.LENGTH_SHORT).show(); }
          else  if (TextUtils.isEmpty(Location)) {
              Toast.makeText(this, "Please enter a Location ", Toast.LENGTH_SHORT).show(); }
          else  if (TextUtils.isEmpty(Date)) {
              Toast.makeText(this, "Please enter a Date ", Toast.LENGTH_SHORT).show();}

          else {
              SaveToDatabase();
          }
      }

      /**Creates a database ref , saves the  volunteer  info to the database*/
      private void SaveToDatabase() {
          Calendar calForDate = Calendar.getInstance();
          SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
          String saveCurrentDate = currentDate.format(calForDate.getTime());

          Calendar calForTime = Calendar.getInstance();
          SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
          String saveCurrentTime = currentTime.format(calForTime.getTime());

          String RandomName = saveCurrentDate + saveCurrentTime;

          HashMap postsMap = new HashMap();

          postsMap.put("uid", currentUserId);
          postsMap.put("date", Date);
          postsMap.put("time", Time);
          postsMap.put("title", Title);
          postsMap.put("description", Description);
          postsMap.put("contact", Contact);
          postsMap.put("location", Location);

          VolunteersRef.child(currentUserId + RandomName).updateChildren(postsMap).addOnCompleteListener(new OnCompleteListener() {
              @Override
              public void onComplete(@NonNull Task task) {
                  if (task.isSuccessful()) {

                      Toast.makeText(VolunteerActivity.this, "New Post is updated successfully", Toast.LENGTH_SHORT).show();
                      Intent Intent = new Intent(VolunteerActivity.this, VolunteerHomeActivity.class);
                      startActivity(Intent);
                      finish();}
                  else {
                      String message = task.getException().getMessage();
                      Toast.makeText(VolunteerActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                  }

              }
          });

      }
  }
