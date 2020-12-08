package com.example.icare;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CommentsActivity extends AppCompatActivity {
    /** Declaring variables*/
    private ImageButton PostCommentButton;
    private EditText CommentInputText;
    private RecyclerView CommentsList;
    private String Post_Key, currentUserId, saveCurrentTime, saveCurrentDate, randomKey;
    private DatabaseReference UsersRef, PostsRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

/** Getting Id and assigning them to corresponding variables */

        Post_Key = getIntent().getExtras().get("PostKey").toString();

        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(Post_Key).child("Comments");
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        CommentsList = (RecyclerView) findViewById(R.id.Comments_list);
        CommentsList.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        CommentsList.setLayoutManager(linearLayoutManager);

        CommentInputText = (EditText) findViewById(R.id.comment_input);
        PostCommentButton = (ImageButton) findViewById(R.id.post_comment_button);

/**  Setting event listener to the post comment button and sends it to the Validate comment method */
        PostCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UsersRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String username = snapshot.child("username").getValue().toString();
                            ValidateComment(username);
                            CommentInputText.setText(" ");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }
/** When the application start, comments on each post should inflate on the comments list */
    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Comments> options = new FirebaseRecyclerOptions.Builder<Comments>()
                        .setQuery(PostsRef, Comments.class)
                        .build();

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Comments, CommentsViewHolder>(options) {

            @Override
            public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.all_comments_layout, parent, false);
                return new CommentsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(CommentsViewHolder viewHolder, int i, Comments model) {
                viewHolder.setUsername(model.getUsername());
                viewHolder.setTime(model.getTime());
                viewHolder.setDate(model.getDate());
                viewHolder.setComment(model.getComment());
            }

        };
        adapter.startListening();
        CommentsList.setAdapter(adapter);
    }

/** The method validates that the input text box is not empty and stores the HashMap in the database */
    private void ValidateComment(String username) {

        String commentText = CommentInputText.getText().toString();

        if (TextUtils.isEmpty(commentText)) {
            Toast.makeText(this, "Please write a comment ", Toast.LENGTH_SHORT).show();
        }
        else {

            Calendar calForDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
            saveCurrentDate = currentDate.format(calForDate.getTime());

            Calendar calForTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
            saveCurrentTime = currentTime.format(calForTime.getTime());

            randomKey = currentUserId + saveCurrentDate + saveCurrentTime;

            HashMap CommentsMap = new HashMap();

            CommentsMap.put("uid", currentUserId);
            CommentsMap.put("comment", commentText);
            CommentsMap.put("date", saveCurrentDate);
            CommentsMap.put("time", saveCurrentTime);
            CommentsMap.put("username", username);

            PostsRef.child(randomKey).updateChildren(CommentsMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(CommentsActivity.this, "Comment posted", Toast.LENGTH_SHORT).show();
                        finish();

                    } else
                        {
                        String message = task.getException().getMessage();
                        Toast.makeText(CommentsActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

/**  This class uses the Java Comment class to read and inflate the  all_comments layout*/

    public static class CommentsViewHolder extends RecyclerView.ViewHolder {
    /** Declaring variables*/
    View mView;
        String currentUserId;
        DatabaseReference CommentsRef;

        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
            CommentsRef = FirebaseDatabase.getInstance().getReference().child("Comments");
            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        public void setComment(String comment) {
            TextView Comment = (TextView) mView.findViewById(R.id.comment_Text);
            Comment.setText(comment);
        }

        public void setDate(String date) {
            TextView Date = (TextView) mView.findViewById(R.id.comment_date);
            Date.setText(date);
        }

        public void setTime(String time) {
            TextView Time = (TextView) mView.findViewById(R.id.comment_time);
            Time.setText(time);
        }

        public void setUsername(String username) {
            TextView Username = (TextView) mView.findViewById(R.id.comment_username);
            Username.setText(username);
        }
    }
}






