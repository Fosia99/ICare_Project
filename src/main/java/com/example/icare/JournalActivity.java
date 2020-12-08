package com.example.icare;


import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Date;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class JournalActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }
/** Method for deleting an item on the list once swiped left*/
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            if (direction == ItemTouchHelper.LEFT) {
                NoteAdapter.NoteViewHolder noteViewHolder = (NoteAdapter.NoteViewHolder) viewHolder;
                noteViewHolder.deleteItem();
            }
        }
/** Changes the color and displays a delete icon once an item is swiped */
        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(JournalActivity.this, R.color.colorPrimaryDark))
                    .addActionIcon(R.drawable.icon_delete)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };
    private NoteAdapter noteAdapter;
    /** Declaring variables*/
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);

/** Getting Id and assigning them to corresponding variables */

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(" My Notes");
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        findViewById(R.id.fab).setOnClickListener(v -> showDialog());
    }

   /** This method displays the dialog */
   private void showDialog() {
        MyDialog dialog = new MyDialog();
        dialog.show(getSupportFragmentManager(), MyDialog.DIALOG_NOTE_ADD);
        dialog.setOnDialogClickListener(this::createNote);
    }
    /** This method creates a new on the UserId */
    private void createNote(String text) {
        String userId = FirebaseAuth.getInstance().getUid();

        Note note = new Note(text, false, new Timestamp(new Date()), userId);
        FirebaseFirestore.getInstance().collection("notes").add(note)
                .addOnSuccessListener
                        (documentReference -> Toast.makeText(this, "Note Added", Toast.LENGTH_SHORT).show())
                .addOnFailureListener
                        (e -> Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
    }




/** Checks if there is any user logged it, if not send to the log in activity, if there is inflate the notes for that user */
    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        //if user sign out then this method will be called and user should be null
        if (firebaseAuth.getCurrentUser() == null) {
            openLoginActivity();
            return;
        }
        initNoteListView(firebaseAuth.getCurrentUser());

    }
    /** An intent for the Login Activity*/
    private void openLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    /** Displays the notes of the logged in user on the Notes View list*/
    private void initNoteListView(FirebaseUser user) {
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();

        Query query = rootRef
                .collection("notes")
                .whereEqualTo("userId", user.getUid())
                .orderBy("completed")
                .orderBy("created",Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class)
                .build();

        noteAdapter = new NoteAdapter(options);
        recyclerView.setAdapter(noteAdapter);
        noteAdapter.startListening();

        noteAdapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot snapshot) {
                Note note = snapshot.toObject(Note.class);

                MyDialog dialog = new MyDialog(note.getText());
                dialog.show(getSupportFragmentManager(), MyDialog.DIALOG_NOTE_UPDATE);
                dialog.setOnDialogClickListener(text -> {
                    note.setText(text);
                    snapshot.getReference().set(note);
                });
            }

            public void onCheckBoxClick(boolean isChecked, DocumentSnapshot snapshot) {
                snapshot.getReference().update("completed", isChecked);
            }


            public void onDeleteItem(DocumentSnapshot snapshot) {
                Note note = snapshot.toObject(Note.class);
                snapshot.getReference().delete()
                        .addOnSuccessListener(aVoid -> {
                            Snackbar.make(recyclerView, note.getText(), BaseTransientBottomBar.LENGTH_LONG)
                                    .setAction("Undo", v -> snapshot.getReference().set(note));
                        });
            }
        });
    }
    /** When the app starts, checks if the is a user authenticated*/
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(this);

    }


    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(this);
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            noteAdapter.stopListening();
    }

}
