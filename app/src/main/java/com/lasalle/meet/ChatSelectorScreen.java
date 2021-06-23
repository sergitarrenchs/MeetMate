package com.lasalle.meet;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;
import com.lasalle.meet.enities.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class ChatSelectorScreen extends AppCompatActivity {
    private float x1,x2,y1,y2;

    private User user;
    private static String userId = "USER_ID";

    private FloatingActionButton searchButton;
    private MaterialTextView userName;
    private MaterialTextView welcomeMessage;

    private LocalDateTime currentDate;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_selector_activity);

        user = (User) getIntent().getSerializableExtra(userId);

        userName = (MaterialTextView) findViewById(R.id.profile_name);

        userName.setText(user.getFullName());

        currentDate = LocalDateTime.ofInstant(new Date().toInstant(),
                ZoneId.systemDefault());

        welcomeMessage = (MaterialTextView) findViewById(R.id.welcomeMessage);

        if (currentDate.getHour() >= 23 && currentDate.getHour() <= 5) {
            welcomeMessage.setText("Good Night");
        } else if (currentDate.getHour() >= 20 && currentDate.getHour() <= 22) {
            welcomeMessage.setText("Good Evening");
        } else if (currentDate.getHour() >= 15 && currentDate.getHour() <= 19) {
            welcomeMessage.setText("Good Afternoon");
        } else if (currentDate.getHour() >= 5 && currentDate.getHour() <= 14) {
            welcomeMessage.setText("Good Morning");
        }

        searchButton = (FloatingActionButton) findViewById(R.id.searchFloatingActionButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ChatSelectorScreen.this, ChatScreen.class);
//                intent.putExtra(userId, user);
//                startActivity(intent);
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
                    Intent i = new Intent(ChatSelectorScreen.this, HomeScreen.class);
                    i.putExtra(userId, user);
                    startActivity(i);
                }else if(x1 > x2){
                    Intent i = new Intent(ChatSelectorScreen.this, ProfileScreen.class);
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

        Intent intent = new Intent(ChatSelectorScreen.this, HomeScreen.class);
        intent.putExtra(userId, user);
        startActivity(intent);
    }


}
