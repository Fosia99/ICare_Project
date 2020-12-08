package com.example.icare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {
    /** Declaring variables*/

   private EditText forpassemail;
   private Button forpassresetbut;
   private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        /** Getting Id and assigning them to corresponding variables */
        mAuth = FirebaseAuth.getInstance();
        forpassresetbut = findViewById(R.id.forpassresetbut);

        /**Sets an onclick listener to the rest password button >>> sends to Restpass method*/
        forpassresetbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetpass();
            }
        });
    }

/** Checks if the email being entered is valid and sends a reset password link */
    public void resetpass() {

        forpassemail = findViewById(R.id.forpassmail);
        String email = forpassemail.getText().toString();

        if (email.isEmpty()) {
            forpassemail.setError("Enter an email address");
            forpassemail.requestFocus();
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            forpassemail.setError("Enter a valid email address");
            forpassemail.requestFocus();
        }
        else {
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                        Toast.makeText(getApplicationContext(), "Password reset link sent. Check your inbox",
                                Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}

