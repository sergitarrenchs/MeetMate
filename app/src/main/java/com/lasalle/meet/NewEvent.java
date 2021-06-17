package com.lasalle.meet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompatExtras;

import com.lasalle.meet.enities.User;


public class NewEvent extends AppCompatActivity {
    private ImageButton NextButton;

    private User user;
    private static String userId = "USER_ID";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_event_1_activity);

        user = (User) getIntent().getSerializableExtra(userId);

        NextButton = (ImageButton) findViewById(R.id.next_button_imageView);

        NextButton.setOnClickListener(v -> {
            Intent intent = new Intent(NewEvent.this, FinishEvent.class);
            intent.putExtra(userId, user);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        user.logOutUser();

        Intent intent = new Intent(NewEvent.this, HomeScreen.class);
        intent.putExtra(userId, user);
        startActivity(intent);
    }

}
