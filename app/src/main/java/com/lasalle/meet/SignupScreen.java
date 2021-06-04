package com.lasalle.meet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lasalle.meet.enities.User;
import com.lasalle.meet.exceptions.userexceptions.UserEmailExistException;
import com.lasalle.meet.exceptions.userexceptions.UserEmailNullException;
import com.lasalle.meet.exceptions.userexceptions.UserException;
import com.lasalle.meet.exceptions.userexceptions.UserPasswordLowSecurityException;
import com.lasalle.meet.exceptions.userexceptions.UserPasswordNotEqualException;
import com.lasalle.meet.exceptions.userexceptions.UserPasswordNullException;

public class SignupScreen extends AppCompatActivity{
    private Button LoginOptButton;
    private Button SignUpButton;

    private EditText nameSignUpText;
    private EditText surnameSignUpText;
    private EditText emailSignUpText;
    private EditText passwordSignUpText;
    private EditText passwordRepeatedSignUpText;

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
                Intent intent = new Intent(SignupScreen.this, LoginScreen.class);
                startActivity(intent);
            }
        });

        nameSignUpText = (EditText) findViewById(R.id.username_signup);
        surnameSignUpText = (EditText) findViewById(R.id.fullname_signup);
        emailSignUpText = (EditText) findViewById(R.id.username_login3);
        passwordSignUpText = (EditText) findViewById(R.id.password_editText2);
        passwordRepeatedSignUpText = (EditText) findViewById(R.id.password_editText);
    }

    private void createUser() {
        User user = new User();

        try {
            user.signUpUser(emailSignUpText.getText().toString(),passwordSignUpText.getText().toString(),passwordRepeatedSignUpText.getText().toString(),
                    nameSignUpText.getText().toString(),surnameSignUpText.getText().toString());
        } catch (UserEmailNullException e) {
            emailSignUpText.setError("The email is empty");
        } catch (UserPasswordNullException e) {
            passwordSignUpText.setError("The password is empty");
        } catch (UserPasswordNotEqualException e){
            passwordRepeatedSignUpText.setError("The passwords do not coincide");
        } catch (UserPasswordLowSecurityException e){
            passwordSignUpText.setError("The password has to be minimum of 8 characters long");
        } catch (UserEmailExistException e){
            emailSignUpText.setError("The email already exist");
        } catch (UserException ignored){}
    }
}
