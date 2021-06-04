package com.lasalle.meet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignupScreen extends AppCompatActivity{
    public Button LoginOptButton;
    public Button SignUpButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);

        SignUpButton = (Button) findViewById(R.id.signup_button);

        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupScreen.this, HomeScreen.class);
                startActivity(intent);
            }
        });

        LoginOptButton = (Button) findViewById(R.id.login_button);

        LoginOptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupScreen.this, SignupScreen.class);
                startActivity(intent);
            }
        });
    }
}
