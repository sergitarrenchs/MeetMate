package com.lasalle.meet;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textview.MaterialTextView;
import com.lasalle.meet.enities.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class ChatScreen extends AppCompatActivity {
    private float x1,x2,y1,y2;

    private User user;
    private static String userId = "USER_ID";

    private MaterialTextView userName;
    private MaterialTextView welcomeMessage;

    private LocalDateTime currentDate;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        user = (User) getIntent().getSerializableExtra(userId);

        userName = (MaterialTextView) findViewById(R.id.edit_profile_name);



        currentDate = LocalDateTime.ofInstant(new Date().toInstant(),
                ZoneId.systemDefault());

        welcomeMessage = (MaterialTextView) findViewById(R.id.welcomeMessage);

        userName.setText(user.getFullName());

        if (currentDate.getHour() >= 23 && currentDate.getHour() <= 5) {
            welcomeMessage.setText("Good Night");
        } else if (currentDate.getHour() >= 20 && currentDate.getHour() <= 22) {
            welcomeMessage.setText("Good Evening");
        } else if (currentDate.getHour() >= 15 && currentDate.getHour() <= 19) {
            welcomeMessage.setText("Good Afternoon");
        } else if (currentDate.getHour() >= 5 && currentDate.getHour() <= 14) {
            welcomeMessage.setText("Good Morning");
        }

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
                    Intent i = new Intent(ChatScreen.this, HomeScreen.class);
                    i.putExtra(userId, user);
                    startActivity(i);
                }else if(x1 > x2){
                    Intent i = new Intent(ChatScreen.this, ProfileScreen.class);
                    i.putExtra(userId, user);
                    startActivity(i);
                }
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        user.logOutUser();

        Intent intent = new Intent(ChatScreen.this, HomeScreen.class);
        intent.putExtra(userId, user);
        startActivity(intent);
    }


}
