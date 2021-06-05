package com.lasalle.meet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileScreen extends AppCompatActivity {
    public Button LogOutButton;
    public Button DeleteAccButton;


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
}
