package com.lasalle.meet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.lasalle.meet.enities.User;


public class EventTimeline extends AppCompatActivity {

    private User user;
    private static String userId = "USER_ID";

    private MaterialButton allEventsButton;
    private MaterialButton yoursEventsButton;
    private MaterialButton othersEventsButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline_activity);

        user = (User) getIntent().getSerializableExtra(userId);

        allEventsButton = (MaterialButton) findViewById(R.id.all_events_button);
        allEventsButton.setBackgroundColor(getResources().getColor(R.color.white));

        allEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allEventsButton.setBackgroundColor(getResources().getColor(R.color.white));
                yoursEventsButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                othersEventsButton.setBackgroundColor(getResources().getColor(R.color.transparent));

            }
        });

        yoursEventsButton = (MaterialButton) findViewById(R.id.yours_events_button);
        yoursEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allEventsButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                yoursEventsButton.setBackgroundColor(getResources().getColor(R.color.white));
                othersEventsButton.setBackgroundColor(getResources().getColor(R.color.transparent));

            }
        });

        othersEventsButton = (MaterialButton) findViewById(R.id.others_events_button);
        othersEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allEventsButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                yoursEventsButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                othersEventsButton.setBackgroundColor(getResources().getColor(R.color.white));

            }
        });

    }

    @Override
    public void onBackPressed() {
        user.logOutUser();

        Intent intent = new Intent(EventTimeline.this, HomeScreen.class);
        intent.putExtra(userId, user);
        startActivity(intent);
    }
}
