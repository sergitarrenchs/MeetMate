package com.lasalle.meet;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.lasalle.meet.enities.User;


public class EventTimeline extends AppCompatActivity {

    private User user;
    private static String userId = "USER_ID";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline_activity);

        user = (User) getIntent().getSerializableExtra(userId);
    }

    @Override
    public void onBackPressed() {
        user.logOutUser();

        Intent intent = new Intent(EventTimeline.this, HomeScreen.class);
        intent.putExtra(userId, user);
        startActivity(intent);
    }
}
