package com.lasalle.meet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompatExtras;

import com.google.android.material.textfield.TextInputEditText;
import com.lasalle.meet.enities.User;


public class NewEvent extends AppCompatActivity {
    private ImageButton NextButton;

    private User user;
    private static String userId = "USER_ID";

    private TextInputEditText eventName;
    private TextInputEditText eventDescription;
    private TextInputEditText eventStartDate;
    private TextInputEditText eventEndDate;
    private TextInputEditText eventType;
    private TextInputEditText eventMaxParticipants;

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

        eventName = (TextInputEditText) findViewById(R.id.event_name_signup2);
        eventDescription = (TextInputEditText) findViewById(R.id.event_name_signup);
        eventStartDate = (TextInputEditText) findViewById(R.id.event_name_signup3);
        eventEndDate = (TextInputEditText) findViewById(R.id.event_name_signup4);
        eventType = (TextInputEditText) findViewById(R.id.event_name_signup6);
        eventMaxParticipants = (TextInputEditText) findViewById(R.id.event_name_signup5);
    }

    @Override
    public void onBackPressed() {
        user.logOutUser();

        Intent intent = new Intent(NewEvent.this, HomeScreen.class);
        intent.putExtra(userId, user);
        startActivity(intent);
    }

}
