package com.example.icare;

import android.content.Context;
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
import java.util.Map;

public class FriendsActivity extends AppCompatActivity {
    /** Declaring variables*/
    private RecyclerView friendList;
    private DatabaseReference FriendsRef, UserRef;
    private FirebaseAuth mAuth;
    private String UserId;
    private Button findFriends;
    private TextView numOfFriends;
    private  int countFriends;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

/** Getting Id and assigning them to corresponding variables */
        mAuth = FirebaseAuth.getInstance();
        UserId = mAuth.getCurrentUser().getUid();

        mToolbar = (Toolbar) findViewById(R.id.friends_app_bar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle("Friends");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FriendsRef = FirebaseDatabase.getInstance().getReference().child("Friends").child(UserId);

        numOfFriends = (TextView) findViewById(R.id.number_of_friends);
        findFriends = (Button) findViewById(R.id.find_friends);
        friendList = (RecyclerView) findViewById(R.id.friend_list);
        friendList.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        friendList.setLayoutManager(linearLayoutManager);

        DisplayAllFriends();

        /**  Setting event listener to layouts and buttons */
        findFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendToFindFriendsActivity();
            }
        });

        FriendsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    countFriends = (int) snapshot.getChildrenCount();
                    numOfFriends.setText(Integer.toString(countFriends) + " Friend");
                } else {
                    numOfFriends.setText("0 Friends");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**  Controls the users Status that stores their last seen to the database*/
    public void  updateUserStatus( String state){

            Calendar calForDate = Calendar.getInstance();
            SimpleDateFormat currentDate =    new SimpleDateFormat("dd-MMMM-yyyy");
            String saveCurrentDate= currentDate.format(calForDate.getTime());

            Calendar calForTime = Calendar.getInstance();
            SimpleDateFormat currentTime =    new SimpleDateFormat("HH:mm");
            String  saveCurrentTime= currentTime.format(calForTime.getTime());

            Map currentStateMap = new HashMap();
            currentStateMap.put("time",saveCurrentTime);
            currentStateMap.put("date",saveCurrentDate);
            currentStateMap.put("type",state);

            UserRef.child(UserId).child("userState").updateChildren(currentStateMap);

        }

    @Override
    protected void onStart() {
        super.onStart();
        updateUserStatus("online");

    }

    @Override
    protected void onStop() {
        super.onStop();
        updateUserStatus("offline");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateUserStatus("offline");
    }

    private void DisplayAllFriends() {


        FirebaseRecyclerOptions<Friends> options = new FirebaseRecyclerOptions.Builder<Friends>()
                        .setQuery(FriendsRef, Friends.class)
                        .build();

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Friends, FriendsViewHolder>(options) {
            @Override
            public FriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.all_users_display_layout, parent, false);

                return new FriendsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(final FriendsViewHolder viewHolder, final int position, Friends model) {
                final String user_id = getRef(position).getKey();


                UserRef.child(user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())

                        { /** Reads the Friends's infor from the databse and display on the layout  */

                            final String userName = snapshot.child("fullname").getValue().toString();
                            final String profileImage = snapshot.child("profileimage").getValue().toString();
                            final String status = snapshot.child("profilestatus").getValue().toString();

                            viewHolder.setFullname(userName);
                            viewHolder.setStatus(status);
                            viewHolder.setProfileimage(getApplicationContext(),profileImage);

                            /**  Sets an onclick listener on the viewholder and a dialog pops ups*/
                            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    CharSequence options[] = new CharSequence[]
                                            {
                                                "View  " +  userName + "'s profile",
                                                    "Send  " + userName + "  a" + " message"

                                            };
                                    AlertDialog.Builder  builder = new AlertDialog.Builder(FriendsActivity.this);
                                    builder.setTitle("Select option");


                                    builder.setItems(options, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            if (which == 0)
                                            {
                                                 Intent profileIntent = new Intent(FriendsActivity.this,ProfilePersonalActivity.class);
                                                profileIntent.putExtra("clicked_user_id",user_id);
                                                 startActivity(profileIntent);
                                            }
                                        if (which == 1)
                                            {
                                                Intent chatIntent = new Intent(FriendsActivity.this, ChatActivity.class);
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
        friendList.setAdapter(adapter);

    }

    /** Intents to send from one Activity to another */
    private void SendToFindFriendsActivity() {

        Intent FriendsIntent = new Intent(FriendsActivity.this, FindFriendsActivity.class);
        startActivity(FriendsIntent);
        finish();
    }

    /**  This class uses the Java Friends class to read and inflate the  friends layout*/
    public static class FriendsViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public FriendsViewHolder(View itemView) {
            /** Declaring variables*/
            super(itemView);
            mView = itemView;
        }

        public void setFullname(String fullname) {
            TextView username = (TextView) mView.findViewById(R.id.all_users_full_name);
            username.setText(fullname);
        }
        public void setProfileimage(Context applicationContext, String profileimage) {
            ImageView image = (ImageView) mView.findViewById(R.id.all_users_profile_image);
            Picasso.get().load(profileimage).into(image);

        }
        public void setStatus( String status) {
            TextView Mystatus = (TextView) mView.findViewById(R.id.all_users_status);
            Mystatus.setText(status);

        }
}

}

