package com.example.icare;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

public class ScheduleActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    /** Declaring variables*/
    private RecyclerView mRecyclerView;
    private Exampleadapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Exampleitem> mexamplelist, mreminderlist;
    private TextView tv, tv2;
    private final String Channel_ID = "My_Channel";
    private final int Notification_ID = 001;
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }
        /** Method that delets the item from the list that is swiped*/
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            Exampleitem curitem = mexamplelist.get(position);
            String delid = curitem.getFull();
            FirebaseUser curuser = FirebaseAuth.getInstance().getCurrentUser();
            String uid = curuser.getUid();
            if (now == 2) {
                db = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Reminder").child(delid);
            } else {
                db = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Task").child(delid);
            }
            db.setValue(null);
            mexamplelist.remove(position);
            mAdapter.notifyDataSetChanged();
        }
    };
    boolean doubleBackToExitPressedOnce = false;
    private DatabaseReference db, dbb;
    private LinearLayout notaskll;
    public static int now = 0, firstrun = 0;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        /** Getting Id and assigning them to corresponding variables */
        settvcolor();
        mexamplelist = new ArrayList<>();
        mreminderlist = new ArrayList<>();
        notaskll = findViewById(R.id.notask);
        notaskll.setVisibility(View.GONE);

        /**On starts,loads the tasks , by reading from the database by the current user logged in ID */
        if (firstrun == 0) {
            progressDialog = new ProgressDialog(ScheduleActivity.this);
            progressDialog.setTitle("Loading");
            progressDialog.setMessage("Getting your tasks...");
            progressDialog.show();
            Runnable progressRunnable = new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                }
            };
            Handler pdCanceller = new Handler();
            pdCanceller.postDelayed(progressRunnable, 4500);
        }
        firstrun++;
        FirebaseUser curuser = FirebaseAuth.getInstance().getCurrentUser();
        if (curuser != null) {
            String uid = curuser.getUid();

            /** If the layout is swiped, on Reminder, Display the reminders of the current user*/
            if (now == 2) {
                db = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Reminder");
                db.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<String> arr = new ArrayList<String>();
                        int k;
                        for (k = 0; k < mexamplelist.size(); k++) {
                            arr.add(mexamplelist.get(k).getFull());
                        }
                        k = 0;
                        for (DataSnapshot childsnap : dataSnapshot.getChildren()) {
                            String date = childsnap.getKey();
                            HashMap<String, String> hmp;
                            hmp = (HashMap<String, String>) childsnap.getValue();
                            Boolean exist = arr.contains(date);
                            if (exist == false) {
                                k++;
                                mexamplelist.add(new Exampleitem(hmp.get("title"), hmp.get("des"), hmp.get("date"), hmp.get("time"), date, hmp.get("repeat"), hmp.get("marker")));
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

            }   /** If the layout is not swiped,  Display the tasks of the current user*/
            else {
                db = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Task");
                db.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<String> arr = new ArrayList<String>();
                        int k;
                        for (k = 0; k < mexamplelist.size(); k++) {
                            arr.add(mexamplelist.get(k).getFull());
                        }
                        k = 0;
                        for (DataSnapshot childsnap : dataSnapshot.getChildren()) {
                            String date = childsnap.getKey();
                            HashMap<String, String> hmp;
                            hmp = (HashMap<String, String>) childsnap.getValue();
                            Boolean exist = arr.contains(date);
                            if (exist == false) {
                                k++;
                                mexamplelist.add(new Exampleitem(hmp.get("title"), hmp.get("des"), hmp.get("date"), hmp.get("time"), date, hmp.get("repeat"), hmp.get("marker")));
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        }
        buildrecylerview();

        /**Send to  Refresh task ,if on the Task Layout*/
        if (now != 2) {
            refreshTask();
        }

        /** Add a new task if the add task icon is clicked*/
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ScheduleActivity.this, TaskAdderActivity.class);
                intent.putExtra("now", now);
                startActivity(intent);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout1);
        NavigationView navigationView = findViewById(R.id.nav_view1);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void refreshTask() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    /** get current time,compares with the tasks of the current user, if time has not passed show notification.
                     if time has  passed and does not repeat, move it to past tasks*/

                    public void run() {
                        final String s = getTimeMethod("dd-MMM-yy-hh-mm-ss a");

                        if (s.substring(16, 18).equals("00")) {
                            final String curtime = process(s);
                            mexamplelist = new ArrayList<>();

                            /** Gets id of the current user logged in*/
                            FirebaseUser curuser = FirebaseAuth.getInstance().getCurrentUser();
                            final String uid = curuser.getUid();

                            if (curuser != null) {
                                db = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Task");
                                db.addValueEventListener(new ValueEventListener() {

                                    @RequiresApi(api = Build.VERSION_CODES.N)
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        List<String> arr = new ArrayList<String>();
                                        int k;
                                        for (k = 0; k < mexamplelist.size(); k++) {
                                            arr.add(mexamplelist.get(k).getFull());
                                        }
                                        k = 0;
                                        for (DataSnapshot childsnap : dataSnapshot.getChildren()) {
                                            String date = childsnap.getKey();
                                            HashMap<String, String> hmp;
                                            hmp = (HashMap<String, String>) childsnap.getValue();
                                            String tasktime = date.substring(0, 12);

                                            /** Compares the current time and task time, to show notification*/
                                            if (curtime.compareTo(tasktime) == 0) {
                                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                                                    showNotification1();
                                                } else {
                                                    showNotification2();
                                                } }
                                            /**Compares if it repeats if the currenttime == task time*/

                                                else if (curtime.compareTo(tasktime) > 0) {
                                                String repeat = hmp.get("repeat");
                                                if (repeat.equals("None")) {

                                                    dbb = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Pasttask");

                                                    /** creates a hashmap with task info and save  it under PastTask in the database*/

                                                    Map<String, Object> val = new TreeMap<>();
                                                    Info info = new Info(hmp.get("title"), hmp.get("des"), hmp.get("date"), hmp.get("time"), "None", date, hmp.get("marker"));
                                                    val.put(date, info);
                                                    dbb.updateChildren(val);

                                                    /**updates the user's node were the task was moved to past task child*/

                                                    db = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Task").child(date);
                                                    db.setValue(null);
                                                }
                                                else {
                                                    /**If the task repeats, create  a proccess of repeating the task*/
                                                    ArrayList<Integer> dates;

                                                    Process p = new Process();
                                                    dates = p.getdatelist(hmp.get("date"));
                                                    int y = dates.get(0);
                                                    int m = dates.get(1) + 1;
                                                    int d = dates.get(2);
                                                    int x = p.dateToInt(y, m, d);
                                                    dates.clear();

                                                    String curdate = "";
                                                    /**if it repeats daily, it runs everyday*/
                                                    if (repeat.equals("Daily")) {
                                                        x++;
                                                        dates = p.intToDate(x);
                                                        y = dates.get(0);
                                                        m = dates.get(1);
                                                        d = dates.get(2);

                                                    }  /**if it repeats weekly, it runs every 7th day */
                                                    else if (repeat.equals("Weekly")) {
                                                        x += 7;
                                                        dates = p.intToDate(x);
                                                        y = dates.get(0);
                                                        m = dates.get(1);
                                                        d = dates.get(2);
                                                        /**if it repeats monthly, it runs every 12th month */
                                                    } else if (repeat.equals("Monthly")) {
                                                        if (m == 12) {
                                                            m = 1;
                                                            y++;
                                                        } else {
                                                            m++;
                                                        }
                                                    } else {
                                                        y++;
                                                    }
                                                    String ys = Integer.toString(y);
                                                    String ms = Integer.toString(m - 1);
                                                    String ds = Integer.toString(d);
                                                    if (ms.length() != 2)
                                                        ys += "0";
                                                    if (ds.length() != 2)
                                                        ms += "0";
                                                    curdate = ys + ms + ds;

                                                    Map<String, Object> val2 = new TreeMap<>();
                                                    String date2 = curdate + hmp.get("time") + date.substring(12, 15);

                                                    Info info2 = new Info(hmp.get("title"), hmp.get("des"), curdate, hmp.get("time"), repeat, date2, hmp.get("marker"));
                                                    val2.put(date2, info2);
                                                    db = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Task").child(date);
                                                    db.setValue(null);

                                                    dbb = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Task");
                                                    dbb.updateChildren(val2);
                                                }
                                            }
                                            Boolean exist = arr.contains(date);
                                            if (exist == false) {
                                                k++;
                                                mexamplelist.add(new Exampleitem(hmp.get("title"), hmp.get("des"), hmp.get("date"), hmp.get("time"), date, hmp.get("repeat"), hmp.get("marker")));
                                                mAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                            buildrecylerview();
                        }
                    }

                    ;
                });
            }
        }, 0, 1000);
    }

    /** Method that displays notification for schedule*/
    private void showNotification1() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, Channel_ID);
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setContentTitle("Icare");
        builder.setContentText("You have task now.Click to open app.");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat compat = NotificationManagerCompat.from(this);
        compat.notify(Notification_ID, builder.build());
    }

    /** Method that doesnt notification for schedule*/
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotification2() {
        String id = "main_channel";
        NotificationManager notificationManager = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        CharSequence name = "Channel Name";
        String description = "Channel Description";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel notificationChannel = new NotificationChannel(id, name, importance);

        notificationChannel.setDescription(description);
        notificationChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(notificationChannel);
        }
        Intent resintent = new Intent(this, ScheduleActivity.class);
        PendingIntent respenindent = PendingIntent.getActivity(this, 1, resintent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, id);
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setContentTitle("My Schedule");
        builder.setContentText("You have task now. Tap to open app.");
        builder.setDefaults(Notification.DEFAULT_SOUND);

        builder.setContentIntent(respenindent);
        builder.setAutoCancel(true);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(1000, builder.build());
    }

///** Method to remove a shedule once swiped*/
    public void removeitem(int position) {
        Exampleitem curitem = mexamplelist.get(position);
        String delid = curitem.getFull();

        FirebaseUser curuser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = curuser.getUid();
        db = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Task").child(delid);
        db.setValue(null);

        mexamplelist.remove(position);
        mAdapter.notifyDataSetChanged();
    }

    /** Method to clear Tasks / Reminder once swiped*/

    public void cleartask(View view) {


        AlertDialog.Builder dialog = new AlertDialog.Builder(ScheduleActivity.this);
        dialog.setTitle("Are you sure?");
        dialog.setMessage("This action can't be reversed");

        dialog.setPositiveButton("Clear", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseUser curuser = FirebaseAuth.getInstance().getCurrentUser();
                if (curuser != null) {
                    String uid = curuser.getUid();
                    if (now == 2) {
                        db = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Reminder");
                    }
                    else
                        {
                        db = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Task");
                         }
                    mexamplelist.clear();
                }
                buildrecylerview();
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

    /** Method assigns id, layout manager to the recycler viw and passes data from the adapter and display on the main list */
    public void buildrecylerview() {

        mRecyclerView = findViewById(R.id.mainll);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new Exampleadapter(mexamplelist);
        mRecyclerView.setLayoutManager(mLayoutManager);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new Exampleadapter.OnItemClickListener() {
            @Override
            /** Sets an onclick listener to items on the lists, and shows a pop when clicked with details of the task*/
            public void onitemclick(int position) {
                Intent intent = new Intent(ScheduleActivity.this, PopupActivity.class);
                Exampleitem e = mexamplelist.get(position);
                intent.putExtra("title", e.getTitle());
                intent.putExtra("date", e.getDate());
                intent.putExtra("time", e.getTime());
                intent.putExtra("des", e.getDes());
                intent.putExtra("repeat", e.getRepeat());
                intent.putExtra("marker", e.getMarker());
                startActivity(intent);
            }

            @Override
            public void ondelete(int position) {
                removeitem(position);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }

    /**  Setting event listener to the items on the Menu*/
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();

                 if (id == R.id.nav_past) {
                    Intent intent = new Intent(ScheduleActivity.this, Past_TasksActivity.class);
                    startActivity(intent);
                }
                 else if (id == R.id.nav_calender) {
                     Intent intent = new Intent(ScheduleActivity.this, CalenderActivity.class);
                    startActivity(intent);

                } else if (id == R.id.nav_share) {
                    Intent intent = new Intent(ScheduleActivity.this, InviteActivity.class);
                    startActivity(intent);}

                 else if (id == R.id.nav_prof) {
                         Intent intent = new Intent(ScheduleActivity.this, ProfileActivity.class);
                         startActivity(intent);
                }

                DrawerLayout drawer = findViewById(R.id.drawer_layout1);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }

            /**Changes text color for Task View */
            public void onTimetask(View view) {
                TextView ttv = findViewById(R.id.timetasktv);
                TextView rtv = findViewById(R.id.remindertv);
                ttv.setTextColor(Color.parseColor("#C71585"));
                rtv.setTextColor(Color.parseColor("#000000"));
                now = 1;
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
    /**Changes text color for Reminder View */
            public void onReminder(View view) {
                TextView ttv = findViewById(R.id.timetasktv);
                TextView rtv = findViewById(R.id.remindertv);
                ttv.setTextColor(Color.parseColor("#000000"));
                rtv.setTextColor(Color.parseColor("#000000"));
                now = 2;
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
    /**Changes text color for Views */
            private void settvcolor() {
                TextView ttv = findViewById(R.id.timetasktv);
                TextView rtv = findViewById(R.id.remindertv);
                if (now == 2) {
                    ttv.setTextColor(Color.parseColor("#000000"));
                    rtv.setTextColor(Color.parseColor("#000000"));
                } else {
                    ttv.setTextColor(Color.parseColor("#000000"));
                    rtv.setTextColor(Color.parseColor("#000000"));
                }
            }

            /** Passed these date strings to the Process Class*/
            private String process(String s) {
                String f = "20" + s.substring(7, 9);
                String month = s.substring(3, 6);
                if (month.equals("Jan")) {
                    f += "00";
                } else if (month.equals("Feb")) {
                    f += "01";
                } else if (month.equals("Mar")) {
                    f += "02";
                } else if (month.equals("Apr")) {
                    f += "03";
                } else if (month.equals("May")) {
                    f += "04";
                } else if (month.equals("Jun")) {
                    f += "05";
                } else if (month.equals("Jul")) {
                    f += "06";
                } else if (month.equals("Aug")) {
                    f += "07";
                } else if (month.equals("Sep")) {
                    f += "08";
                } else if (month.equals("Oct")) {
                    f += "09";
                } else if (month.equals("Nov")) {
                    f += "10";
                } else {
                    f += "11";
                }
                f += s.substring(0, 2);
                String h = s.substring(10, 12);
                int hr = Integer.parseInt(h);
                if (s.charAt(19) == 'P') {
                    hr += 12;
                }
                String hour = String.valueOf(hr);
                if (hour.length() == 1) {
                    f += "0";
                }
                f += hour;
                f += s.substring(13, 15);
                return f;
            }

            private String getTimeMethod(String formate) {
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat(formate);
                String formattedDate = dateFormat.format(date);
                return formattedDate;
            }

        }





