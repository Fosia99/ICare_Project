package com.example.icare;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VolunteerHomeActivity extends AppCompatActivity {
    /** Declaring variables*/
    private FirebaseAuth mAuth;
    private String currentUserId;
    private TextView content,name,location;
    private FloatingActionButton addVolu;
    private ImageView image;
    private Toolbar mToolbar;
    private DatabaseReference VolunteerRef,UserRef;
    private RecyclerView VolunteerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_home);

        /** Getting Id and assigning them to corresponding variables */
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        VolunteerRef = FirebaseDatabase.getInstance().getReference().child("Volunteer");

        mToolbar = (Toolbar) findViewById(R.id.volunteer_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Volunteer Opportunities");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addVolu = (FloatingActionButton) findViewById(R.id.add_volunteer);
        VolunteerList = (RecyclerView) findViewById(R.id.volunteer_list);
        VolunteerList.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        VolunteerList.setLayoutManager(linearLayoutManager);

        DisplayAllVolunteers();

        /** Sets an event listener to the add button and sends it to the Volunteer activity */
        addVolu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(VolunteerHomeActivity.this,VolunteerActivity.class);
                startActivity(Intent);
            }
        });
    }

    /** This display all the Volunteering opportunities in the Volunteer layout */
    private void DisplayAllVolunteers() {
        FirebaseRecyclerOptions<Volunteer> options = new FirebaseRecyclerOptions.Builder<Volunteer>()
                        .setQuery(VolunteerRef, Volunteer.class)
                        .build();

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Volunteer, VolunteerViewHolder>(options) {
            @Override
            public VolunteerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.volunteer_layout, parent, false);

                return new VolunteerViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(final VolunteerViewHolder viewHolder, final int position, Volunteer model) {
                final String user_id = getRef(position).getKey();

                viewHolder.setTime(model.getTime());
                viewHolder.setTitle(model.getTitle());
                viewHolder.setContent(model.getContent());
                viewHolder.setDate(model.getDate());
                viewHolder.setLocation(model.getLocation());
                viewHolder.setContact(model.getContact());
                VolunteerRef.child(user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())

                        {
                            final String VTime= snapshot.child("time").getValue().toString();
                            final String VTitle = snapshot.child("title").getValue().toString();
                            final String VLocation = snapshot.child("location").getValue().toString();
                            final String VContact = snapshot.child("contact").getValue().toString();
                            final String VDate = snapshot.child("date").getValue().toString();
                            final String VDescription = snapshot.child("description").getValue().toString();

                            viewHolder.setContent(VDate);
                            viewHolder.setContent(VTime);
                            viewHolder.setContent(VTitle);
                            viewHolder.setContent(VContact);
                            viewHolder.setContent(VLocation);
                            viewHolder.setContent(VDescription);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        };
        adapter.startListening();
        VolunteerList.setAdapter(adapter);
    }

    /**  This class uses the Java Voulnteer class to read and inflate the Volunteer layout*/
    public static class VolunteerViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public VolunteerViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setTitle(String title) {
            TextView VolnTitle = (TextView) mView.findViewById(R.id.volunteer_title_dis);
            VolnTitle.setText(title);
        }
        public void setLocation(String location) {
            TextView VolnLocation = (TextView) mView.findViewById(R.id.volnteer_location_dis);
            VolnLocation.setText(location);
        }
        public void setContent(String content) {
            TextView VolnContent = (TextView) mView.findViewById(R.id.volunteer_content_dis);
            VolnContent.setText(content);
        }
        public void setContact(String contact) {
            TextView VolnContact = (TextView) mView.findViewById(R.id.volunteer_contact_dis);
            VolnContact.setText(contact);
        }
        public void setDate(String date) {
            TextView VolnDate = (TextView) mView.findViewById(R.id.volnteer_date_dis);
            VolnDate.setText(date);
        }
        public void setTime(String time) {
            TextView VolnTime = (TextView) mView.findViewById(R.id.volnteer_time_dis);
            VolnTime.setText(time);
        }
    }
}






