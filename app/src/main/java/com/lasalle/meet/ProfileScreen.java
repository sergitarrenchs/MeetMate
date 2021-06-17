package com.lasalle.meet;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileScreen extends AppCompatActivity {
    public Button LogOutButton;
    public Button DeleteAccButton;

    float x1,x2,y1,y2;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        LogOutButton = (Button) findViewById(R.id.logout_button);

        LogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileScreen.this, LoginScreen.class);
                startActivity(intent);
            }
        });

        DeleteAccButton = (Button) findViewById(R.id.delete_account_button);

        DeleteAccButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileScreen.this, SignupScreen.class);
                startActivity(intent);
            }
        });
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
                    startActivity(i);
                }else if(x1 > x2){
                    Intent i = new Intent(ProfileScreen.this, HomeScreen.class);
                    startActivity(i);
                }
                break;
        }
        return false;
    }
}
