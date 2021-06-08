package com.lasalle.meet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompatExtras;


public class NewEvent extends AppCompatActivity {
    public ImageButton NextButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_event_1_activity);

        NextButton = (ImageButton) findViewById(R.id.next_button_imageView);

        NextButton.setOnClickListener(v -> {
            Intent intent = new Intent(NewEvent.this, FinishEvent.class);
            startActivity(intent);
        });
    }

}
