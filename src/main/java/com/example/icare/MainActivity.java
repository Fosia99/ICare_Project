package com.example.icare;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    FirebaseRecyclerAdapter adapter;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private RecyclerView groupsList;
    private Toolbar mToolbar;
    private TextView groupName,groupDescription;
    /** Declaring variables*/
    private NavigationView navigationView;
    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef, LikesRef,GroupsRef;
    private CircleImageView navProfileImage, profileImage;
    private TextView navProfileUserName,mainName;
    private FloatingActionButton AddNewFeature;
    private CircleImageView mygroupIcon;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** Getting Id and assigning them to corresponding variables */

        mToolbar = (Toolbar) findViewById(R.id.main_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Groups");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        mygroupIcon= (CircleImageView) findViewById(R.id.group_icon);
        groupDescription = (TextView) findViewById(R.id.group_description);
        groupName =  (TextView) findViewById(R.id.group_name);
        AddNewFeature= (FloatingActionButton) findViewById(R.id.add_feature);

        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        GroupsRef = FirebaseDatabase.getInstance().getReference().child("Groups");
        LikesRef = FirebaseDatabase.getInstance().getReference().child("Likes");

        groupsList = (RecyclerView) findViewById(R.id.all_groups_list);
        groupsList.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        groupsList.setLayoutManager(linearLayoutManager);


        drawerLayout = (DrawerLayout) findViewById(R.id.drawable_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        View navView = navigationView.inflateHeaderView(R.layout.navigation_header);
        navProfileImage = (CircleImageView) navView.findViewById(R.id.nav_profile_image);
        navProfileUserName = (TextView) navView.findViewById(R.id.nav_user_full_name);


        /** This method reads the logged in user's name and profile image from the database and shows it in the header of the nav*/
        UsersRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.hasChild("fullname")) {
                        String fullname = snapshot.child("fullname").getValue().toString();
                        navProfileUserName.setText(fullname);
                    }
                    if (snapshot.hasChild("profileimage")) {
                        String image = snapshot.child("profileimage").getValue().toString();
                        Picasso.get().load(image).placeholder(R.drawable.profile).into(navProfileImage);


                    } else {
                        SendUserToSetupActivity();

                    }
                }
            }

            @Override

            public void onCancelled(DatabaseError error) {

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
/** Calls the DisplayAllGroups Method*/
        DisplayAllGroups();

        /** Sets an event listener to the create group button*/
        AddNewFeature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SendUserToGroupsActivity();
            }
        });

    }

    /**  Controls the users Status that stores their last seen to the database*/

    public void updateUserStatus(String state) {

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        String saveCurrentDate = currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        String saveCurrentTime = currentTime.format(calForTime.getTime());

        Map currentStateMap = new HashMap();
        currentStateMap.put("time", saveCurrentTime);
        currentStateMap.put("date", saveCurrentDate);
        currentStateMap.put("type", state);

        UsersRef.child(currentUserId).child("userState").updateChildren(currentStateMap);

    }
/** Checks if the is a user logged in, if so send to the CheckUserExistence method*/
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
        groupsList.setAdapter(adapter);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            SendUserToLoginActivity();
        } else {
            CheckUserExistence();
        }


    }

    /** This method checks if the user that is currently logged is already in the User Database, if not send to the SetUp activity */
    private void CheckUserExistence() {
        final String current_user_id = mAuth.getCurrentUser().getUid();

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.hasChild(current_user_id)) {
                    SendUserToSetupActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
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
    /**  Setting event listener to the items on the Main Menu*/
    private void UserMenuSelector(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.nav_about_cancer:
                SendUserToAboutActivity();
                Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_find_friends:
                SendUserToFindFriendsActivity();
                Toast.makeText(this, "Find Friends", Toast.LENGTH_SHORT).show();
                break;
            case R.id.navigation_profile:
                SendUserToProfileActivity();
                Toast.makeText(this, "My Profile", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_settings:
                SendUserToApplicationActivity();
                break;
            case R.id.nav_schedule:
                SendToScheduleActivity();
                Toast.makeText(this, "Schedule", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_journal:
                SendToJournalActivity();
                Toast.makeText(this, "Journal", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_friends:
                Toast.makeText(this, "Friends", Toast.LENGTH_SHORT).show();
                SendUserToFriendsActivity();
                break;
            case R.id.nav_contact_us:
                SendUserToAboutUsActivity();
                Toast.makeText(this, "Contact Us", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_log_out:
                updateUserStatus("offline");
                mAuth.signOut();
                SendUserToLoginActivity();
                break;
        }
    }


    /**  This class uses the Java Group class to read and inflate the  group layout*/
    private void DisplayAllGroups() {
        Query orderGroupsInOrder = GroupsRef.orderByChild("counter");

        FirebaseRecyclerOptions<Groups> options = new FirebaseRecyclerOptions.Builder<Groups>()
                        .setQuery(orderGroupsInOrder, Groups.class)
                        .build();

                      adapter = new FirebaseRecyclerAdapter<Groups, GroupsViewHolder>(options) {
            @Override
            public GroupsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.all_groups_layout, parent, false);

                return new GroupsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(GroupsViewHolder viewHolder, int position, Groups model) {

                final String GroupKey = getRef(position).getKey();


                GroupsRef.child(GroupKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            final String Icon = snapshot.child("groupimages").getValue().toString();

                            viewHolder.setName(model.getName());
                            viewHolder.setDescription(model.getDescription());
                            viewHolder.setGroupIcon(getApplicationContext(), Icon);
                        }

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent ClickGroupIntent = new Intent(MainActivity.this, GroupClickActivity.class);
                                ClickGroupIntent.putExtra("GroupKey", GroupKey);
                                startActivity(ClickGroupIntent);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        };
        adapter.startListening();
        groupsList.setAdapter(adapter);
        updateUserStatus("online");
    }

    /** Intents to send from one Activity to another */
    private void SendToJournalActivity() {
        Intent ChatIntent = new Intent(MainActivity.this, JournalActivity.class);
        startActivity(ChatIntent);}

    private void SendUserToLoginActivity() {
        Intent LoginIntent = new Intent(MainActivity.this, LoginActivity.class);
        LoginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(LoginIntent);
    }

    private void SendUserToApplicationActivity() {
        Intent AppIntent = new Intent(MainActivity.this, ApplicationSettingsActivity.class);
        startActivity(AppIntent);
    }
    private void SendUserToFriendsActivity() {
        Intent FriendsIntent = new Intent(MainActivity.this, FriendsActivity.class);
        startActivity(FriendsIntent);
    }
    private void SendUserToAboutUsActivity() {
        Intent UsIntent = new Intent(MainActivity.this, AboutActivity.class);
        startActivity(UsIntent);
    }
    private void SendUserToFindFriendsActivity() {
        Intent findFriendsIntent = new Intent(MainActivity.this, FindFriendsActivity.class);
        startActivity(findFriendsIntent);
    }
    private void SendUserToProfileActivity() {
        Intent ProfileIntent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(ProfileIntent);
    }
    private void SendToScheduleActivity() {
        Intent ScheduleIntent = new Intent(MainActivity.this, ScheduleActivity.class);
        startActivity(ScheduleIntent);

    }
    private void SendUserToSetupActivity() {
        Intent setUpIntent = new Intent(MainActivity.this, SetupActivity.class);
        setUpIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setUpIntent);
        finish();
    }
    private void SendUserToAboutActivity() {
        Intent AboutIntent = new Intent(MainActivity.this, AboutCancerActivity.class);
        startActivity(AboutIntent);
    }

    private void SendUserToGroupsActivity() {
        Intent GroupIntent = new Intent(MainActivity.this, GroupsActivity.class);
        startActivity(GroupIntent);
    }

    /**  This class uses the Java Group class to read and inflate the  group layout*/
    public static class GroupsViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public GroupsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;


        }

        public void setDescription(String description) {
            TextView GroupDescription = (TextView) mView.findViewById(R.id.display_groupDescription);
            GroupDescription.setText(description);
        }

        public void setGroupIcon(Context applicationContext, String groupIcon) {
            CircleImageView image = (CircleImageView) mView.findViewById(R.id.display_groupIcon);
            Picasso.get().load(groupIcon).into(image);
        }
        public void setName(String name) {
            TextView GroupName = (TextView) mView.findViewById(R.id.display_groupName);
            GroupName.setText(name);
        }
    }
}





