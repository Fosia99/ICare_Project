package com.example.icare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class LoginActivity extends AppCompatActivity {
    /** Declaring variables*/
    private Button LoginButton;
    private EditText UserEmail, UserPassword;
    private TextView NeedNewAccountLink,forgotPasswordLink;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private Boolean EmailAddressChecker;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

/** Getting Id and assigning them to corresponding variables */

        mAuth = FirebaseAuth.getInstance();
        NeedNewAccountLink = (TextView) findViewById(R.id.register_account_link);
        UserEmail = (EditText) findViewById(R.id.login_email);
        UserPassword = (EditText) findViewById(R.id.login_password);
        LoginButton = (Button) findViewById(R.id.login_button);
        loadingBar = new ProgressDialog(this);
        forgotPasswordLink = (TextView) findViewById(R.id.forgot_password_link);


        /** Sets an onclick listener to the rest password button and sends it to the reset password activity*/
        forgotPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendResetPasswordActivity();

            }
        });
        /** Sets an onclick listener to the sign up button and sends it to the register activity*/
        NeedNewAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToRegisterActivity();

            }
        });
        /** Sets an onclick listener to the login button and sends it to the login method  activity*/
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllowUserToLogin();
            }
        });

    }

  /**This method verify the new users email address*/

private  void VerifyEmailAddress() {
       FirebaseUser user = mAuth.getCurrentUser();
       EmailAddressChecker = user.isEmailVerified();
       if (EmailAddressChecker)
       { SendUserToMainActivity(); }

              else      {
           Toast.makeText(this, "PLease verify your account first", Toast.LENGTH_SHORT).show();
           mAuth.signOut(); } }


     /** This method checks if there is user already logged in , if not sends to the log in method*/
    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null)
        {
            SendUserToMainActivity();
        }
    }
/** Method that allows user to login if the email is already verified  */
    private void AllowUserToLogin() {
        String email = UserEmail.getText().toString();
        String password = UserPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your email.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password.", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Login");
            loadingBar.setMessage("Please wait,while logging in your account");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                               VerifyEmailAddress();

                                loadingBar.dismiss();
                            } else {
                                String message = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, "Error" + message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
            }

    }

    /** Intents to send from one Activity to another */
    private void SendResetPasswordActivity() {
        Intent resetPasswordIntent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
        startActivity(resetPasswordIntent);
    }
    private void SendUserToMainActivity() {
        Intent mainActivityIntent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(mainActivityIntent);
        finish();
    }
    private void SendUserToRegisterActivity() {
            Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(registerIntent);

        }
    }