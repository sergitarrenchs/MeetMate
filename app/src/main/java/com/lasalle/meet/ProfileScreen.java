package com.lasalle.meet;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.lasalle.meet.enities.User;

public class ProfileScreen extends AppCompatActivity {
    private MaterialButton LogOutButton;
    private MaterialButton DeleteAccButton;

    private MaterialTextView fullNameText;
    private MaterialTextView emailText;
    private MaterialTextView passwordText;
    private MaterialTextView usernameText;

    private float x1,x2,y1,y2;

    private User user;
    private static String userId = "USER_ID";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        user = (User) getIntent().getSerializableExtra(userId);

        LogOutButton = (MaterialButton) findViewById(R.id.logout_button);

        LogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileScreen.this, LoginScreen.class);
                startActivity(intent);
            }
        });

        DeleteAccButton = (MaterialButton) findViewById(R.id.delete_account_button);

        DeleteAccButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileScreen.this, SignupScreen.class);
                startActivity(intent);
            }
        });

        fullNameText = (MaterialTextView) findViewById(R.id.profile_name);
        fullNameText.setText(user.getFullName());

        emailText = (MaterialTextView) findViewById(R.id.progile_name4);
        emailText.setText(user.getEmail());

        passwordText = (MaterialTextView) findViewById(R.id.progile_name6);
        passwordText.setText(user.getPassword());

        usernameText = (MaterialTextView) findViewById(R.id.progile_name2);
        usernameText.setText(user.getUsername());



    }

    public boolean onTouchEvent(MotionEvent touchEvent){
        switch(touchEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();
                if(x1 < x2){
                    Intent i = new Intent(ProfileScreen.this, ChatScreen.class);
                    i.putExtra(userId, user);
                    startActivity(i);
                }else if(x1 > x2){
                    Intent i = new Intent(ProfileScreen.this, HomeScreen.class);
                    i.putExtra(userId, user);
                    startActivity(i);
                }
                break;
        }
        return false;
    }
}
