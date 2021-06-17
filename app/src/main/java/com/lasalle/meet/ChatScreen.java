package com.lasalle.meet;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;

import com.lasalle.meet.enities.User;

public class ChatScreen extends AppCompatActivity {
    private float x1,x2,y1,y2;

    private User user;
    private static String userId = "USER_ID";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_acticity);

        user = (User) getIntent().getSerializableExtra(userId);
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
