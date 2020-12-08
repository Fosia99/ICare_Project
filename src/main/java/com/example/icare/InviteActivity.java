package com.example.icare;



import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class InviteActivity extends AppCompatActivity {
    /** Declaring variables*/
    private Button bt;
    private TextView tv;
    private ClipboardManager myClipboard;
    private ClipData myClip;
    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        /** Getting Id and assigning them to corresponding variables */
        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        bt = findViewById(R.id.invitecopy);
        tv = findViewById(R.id.invitelink);
/** Setting an onclick listener to the button, that has a hyperlink to the projects repository on GitHub*/
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = tv.getText().toString();
                myClip = ClipData.newPlainText("https://github.com/Fosia99/My_project", text);
                myClipboard.setPrimaryClip(myClip);


                ll = findViewById(R.id.invitefriends);
                Snackbar sb = Snackbar.make(ll, "Link Copied", Snackbar.LENGTH_LONG);
                sb.show();
            }
        });
    }
}
