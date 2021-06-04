package com.lasalle.meet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lasalle.meet.enities.User;
import com.lasalle.meet.exceptions.userexceptions.UserException;

public class SignupScreen extends AppCompatActivity{
    public Button LoginOptButton;
    public Button SignUpButton;

    public EditText nameSignUpText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);

        SignUpButton = (Button) findViewById(R.id.signup_button);

        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(SignupScreen.this, HomeScreen.class);
                //startActivity(intent);
                createUser();
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

        nameSignUpText = (EditText) findViewById(R.id.username_signup);
    }

    private void createUser() {
        User user = new User();

        try {
            user.signUpUser("loca23l@host.com","1234567890","1234567890", nameSignUpText.getText().toString(),"thaht");

        } catch (UserException e) {
            System.out.println("ERROR CREATE USER");
        }
    }
}
