package com.lasalle.meet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;


public class FinishEvent extends AppCompatActivity {
    public ImageButton CreateButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_event_2_activity);

        CreateButton = (ImageButton) findViewById(R.id.create_button_image);

        CreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinishEvent.this, HomeScreen.class);
                startActivity(intent);
            }
        });
    }
}
