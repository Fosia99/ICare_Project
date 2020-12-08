package com.example.icare;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Past_TasksActivity extends AppCompatActivity {
    /** Declaring variables*/
    private RecyclerView mRecyclerView;
    private Exampleadapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Exampleitem> mexamplelist;
    private  DatabaseReference db;
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {
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

            db = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Pasttask").child(delid);
            db.setValue(null);

            mexamplelist.remove(position);
            mAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past__tasks);
        /** Getting Id and assigning them to corresponding variables */
        mexamplelist = new ArrayList<>();
        FirebaseUser curuser = FirebaseAuth.getInstance().getCurrentUser();

        /** Checks if the is a current user logged in, inorder to inflate the user past tasks from the database on the layout*/
        if (curuser != null) {
            String uid = curuser.getUid();
            db = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Pasttask");
            db.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<String> arr = new ArrayList<String>();
                    int k;
                    for (k = 0; k < mexamplelist.size(); k++) {
                        arr.add(mexamplelist.get(k).getTitle());
                    }
                    k = 0;
                    for (DataSnapshot childsnap : dataSnapshot.getChildren()) {
                        String date = childsnap.getKey();
                        HashMap<String, String> hmp;
                        hmp = (HashMap<String, String>) childsnap.getValue();
                        String name = hmp.get("title");
                        Boolean exist = arr.contains(name);
                        if (exist == false) {
                            k++;
                            mexamplelist.add(new Exampleitem(hmp.get("title"), hmp.get("des"), hmp.get("date"), hmp.get("time"), date, "None", hmp.get("marker")));
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

    /** Method for clearing task  from the list*/
    public void removeitem(int position) {
        Exampleitem curitem = mexamplelist.get(position);
        String delid = curitem.getFull();
        FirebaseUser curuser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = curuser.getUid();
        db = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Pasttask").child(delid);
        db.setValue(null);
        mexamplelist.remove(position);
        mAdapter.notifyDataSetChanged();
    }

/** Method assigns id, layout manager to the recycler viw and passes data from the adapter and display on the exampleList */
    public void buildrecylerview() {

        mRecyclerView = findViewById(R.id.pastll);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);

        DividerItemDecoration itemDecoration =
                new DividerItemDecoration(this, ((LinearLayoutManager) mLayoutManager).getOrientation());

        mAdapter = new Exampleadapter(mexamplelist);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(itemDecoration);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);


        /** Sets an onclick listener to items on the lists, and shows a pop when clicked with details of the task*/
        mAdapter.setOnItemClickListener(new Exampleadapter.OnItemClickListener() {
            @Override
            public void onitemclick(int position) {
                Intent intent = new Intent(Past_TasksActivity.this, PopupActivity.class);
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

/** This method is for clearing the whole list once the clear all task button is clicked*/
    public void clearpasttask(View view) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(Past_TasksActivity.this);
        dialog.setTitle("Are you sure?");
        dialog.setMessage("This action can't be reversed");

        dialog.setPositiveButton("Clear", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseUser curuser = FirebaseAuth.getInstance().getCurrentUser();
                if (curuser != null) {
                    String uid = curuser.getUid();
                    db = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Pasttask");
                    db.setValue(null);
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
}


