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

public class JobListingHomeActivity extends AppCompatActivity {

    /** Declaring variables*/
    private FirebaseAuth mAuth;
    private String currentUserId;
    private   FloatingActionButton addJob;
    private ImageView image;
    private Toolbar mToolbar;
    private DatabaseReference JobRef,UserRef;
    private RecyclerView JobList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_listing_home);


/** Getting Id and assigning them to corresponding variables */

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        JobRef= FirebaseDatabase.getInstance().getReference().child("Jobs");

        addJob = (FloatingActionButton) findViewById(R.id.add_job);
        // content =(TextView) findViewById(R.id.test_content) ;
        mToolbar = (Toolbar) findViewById(R.id.job_app_bar);
        setSupportActionBar(mToolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Job Opportunities");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        JobList = (RecyclerView) findViewById(R.id.jobs_list);
        JobList.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        JobList.setLayoutManager(linearLayoutManager);

        DisplayAllJobs();

        /** Sets an event listener to the add button and sends it to the job  listing activity */
        addJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(JobListingHomeActivity.this,JobListingActivity.class);
                startActivity(Intent);
            }
        });

    }
/** This display all the jobs in the job layout */
    private void DisplayAllJobs() {


        FirebaseRecyclerOptions<Job> options =
                new FirebaseRecyclerOptions.Builder<Job>()
                        .setQuery(JobRef, Job.class)
                        .build();

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Job, JobViewHolder>(options) {
            @Override
            public JobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.jobs_layout, parent, false);

                return new JobViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(final JobViewHolder viewHolder, final int position, Job model) {
                final String user_id = getRef(position).getKey();

                viewHolder.setTime(model.getTime());
                viewHolder.setTitle(model.getTitle());
                viewHolder.setContent(model.getContent());
                viewHolder.setDate(model.getDate());
                viewHolder.setContact(model.getContact());
                viewHolder.setLocation(model.getLocation());
                viewHolder.setRequirements(model.getRequirements());

                JobRef.child(user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())

                        {
                            final String VTime= snapshot.child("time").getValue().toString();
                            final String VTitle = snapshot.child("title").getValue().toString();
                            final String VContent = snapshot.child("description").getValue().toString();
                            final String VLocation = snapshot.child("location").getValue().toString();
                            final String VContact = snapshot.child("contact").getValue().toString();
                            final String VDate = snapshot.child("date").getValue().toString();
                            final String VRequirements = snapshot.child("requirements").getValue().toString();

                            viewHolder.setContent(VContent);
                            viewHolder.setContent(VDate);
                            viewHolder.setContent(VTime);
                            viewHolder.setContent(VTitle);
                            viewHolder.setContent(VContact);
                            viewHolder.setContent(VLocation);
                            viewHolder.setContent(VRequirements);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        };
        adapter.startListening();
        JobList.setAdapter(adapter);

    }


    /**  This class uses the Java Job class to read and inflate the jobs layout*/
    public static class JobViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public JobViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTitle(String title) {
            TextView VolnTitle = (TextView) mView.findViewById(R.id.job_title_dis);
            VolnTitle.setText(title);
        }

        public void setLocation(String location) {
            TextView VolnLocation = (TextView) mView.findViewById(R.id.job_location_dis);
            VolnLocation.setText(location);
        }

        public void setContent(String content) {
            TextView VolnContent = (TextView) mView.findViewById(R.id.job_content_dis);
            VolnContent.setText(content);
        }
        public void setDate(String date) {
            TextView VolnDate = (TextView) mView.findViewById(R.id.job_date_dis);
            VolnDate.setText(date);
        }
        public void setTime(String time) {
            TextView VolnTime = (TextView) mView.findViewById(R.id.job_time_dis);
            VolnTime.setText(time);
        }
        public void setContact(String contact) {
            TextView VolnContact = (TextView) mView.findViewById(R.id.job_contact_dis);
            VolnContact.setText(contact);
        }
        public void setRequirements(String requirements) {
            TextView VolnRequir = (TextView) mView.findViewById(R.id.job_req_dis);
            VolnRequir.setText(requirements);
        }
    }
}






