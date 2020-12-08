package com.example.icare;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.squareup.picasso.Picasso;

public class TestimonyMainActivity extends AppCompatActivity {
    /** Declaring variables*/
    private FirebaseAuth mAuth;
    private String currentUserId;
    private TextView content,name;
    private Button btShowmore;
    private FloatingActionButton addTest;
    private ImageView image;
    private Toolbar mToolbar;
    private DatabaseReference TestimoniesRef,UserRef;
    private RecyclerView TestimoniesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testimony_main);

/** Getting Id and assigning them to corresponding variables */
            mAuth = FirebaseAuth.getInstance();
            currentUserId = mAuth.getCurrentUser().getUid();

            UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
            TestimoniesRef = FirebaseDatabase.getInstance().getReference().child("Testimonies");

            addTest = (FloatingActionButton) findViewById(R.id.add_testimony);
            mToolbar = (Toolbar) findViewById(R.id.testi_app_bar);
            setSupportActionBar(mToolbar);

            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle("Testimonies");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            TestimoniesList = (RecyclerView) findViewById(R.id.testimonies_list);
            TestimoniesList.setHasFixedSize(true);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);
            TestimoniesList.setLayoutManager(linearLayoutManager);

        DisplayAllTestimonies();

        /** Sets an event listener to the add button and sends it to the Testimony activity */
       addTest.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
         Intent Intent = new Intent(TestimonyMainActivity.this,TestemoniesActivity.class);
         startActivity(Intent); }
          });
    }
    /** This display all the Testimonies in the Testimonies layout */
        private void DisplayAllTestimonies() {
        FirebaseRecyclerOptions<Testimonies> options =  new FirebaseRecyclerOptions.Builder<Testimonies>()
                            .setQuery(TestimoniesRef, Testimonies.class)
                            .build();

            FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Testimonies, TestimoniesViewHolder>(options) {
                @Override
                public TestimoniesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.testamony_layout, parent, false);

                    return new TestimoniesViewHolder(view);
                }

                @Override
                protected void onBindViewHolder(final TestimoniesViewHolder viewHolder, final int position, Testimonies model) {
                    final String user_id = getRef(position).getKey();

                    viewHolder.setName(model.getName());
                    viewHolder.setImage(model.getImage());
                    viewHolder.setContent(model.getContent());

                    TestimoniesRef.child(user_id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {

                                final String userName = snapshot.child("fullname").getValue().toString();
                                final String profileImage = snapshot.child("profileimage").getValue().toString();
                                final String description = snapshot.child("description").getValue().toString();

                                viewHolder.setName(userName);
                                viewHolder.setImage(profileImage);
                                viewHolder.setContent(description);

                                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        CharSequence options[] = new CharSequence[]
                                                {
                                                        "View  " +  userName + "'s profile",
                                                        "Send Message"
                                                };
                                        AlertDialog.Builder  builder = new AlertDialog.Builder(TestimonyMainActivity.this);
                                        builder.setTitle("Select option");


                                        builder.setItems(options, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                if (which == 0)
                                                {
                                                    Intent profileIntent = new Intent(TestimonyMainActivity.this,ProfilePersonalActivity.class);
                                                    profileIntent.putExtra("clicked_user_id",user_id);
                                                    startActivity(profileIntent);
                                                }
                                                if (which == 1)
                                                {
                                                    Intent chatIntent = new Intent(TestimonyMainActivity.this, ChatActivity.class);
                                                    chatIntent.putExtra("clicked_user_id",user_id);
                                                    chatIntent.putExtra("userName",userName);
                                                    startActivity(chatIntent);
                                                }
                                            }
                                        });
                                        builder.show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            };
            adapter.startListening();
            TestimoniesList.setAdapter(adapter);

        }
    /**  This class uses the Java Testimonies class to read and inflate the Testimonies layout*/
        public static class TestimoniesViewHolder extends RecyclerView.ViewHolder {
            View mView;

            public TestimoniesViewHolder(View itemView) {
                super(itemView);
                mView = itemView;
            }
            public void setName(String name) {
                TextView username = (TextView) mView.findViewById(R.id.test_username);
                username.setText(name);
            }
            public void setImage(String image) {
                ImageView Profileimage = (ImageView) mView.findViewById(R.id.test_profile);
                Picasso.get().load(image).into(Profileimage);

            }
            public void setContent(String content) {
                TextView TestContent = (TextView) mView.findViewById(R.id.test_content);
                TestContent.setText(content);
            }

        }
    }






