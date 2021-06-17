package com.lasalle.meet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.lasalle.meet.enities.User;
import com.lasalle.meet.exceptions.userexceptions.UserEmailExistException;
import com.lasalle.meet.exceptions.userexceptions.UserEmailNullException;
import com.lasalle.meet.exceptions.userexceptions.UserException;
import com.lasalle.meet.exceptions.userexceptions.UserIncorrectCredentialsException;
import com.lasalle.meet.exceptions.userexceptions.UserPasswordLowSecurityException;
import com.lasalle.meet.exceptions.userexceptions.UserPasswordNotEqualException;
import com.lasalle.meet.exceptions.userexceptions.UserPasswordNullException;

public class LoginScreen extends AppCompatActivity {
    private MaterialButton LoginButton;
    private MaterialButton SignUpOptButton;
    private User user;
    private static String userId = "USER_ID";

    private TextInputEditText emailLogInText;
    private TextInputEditText passwordLogInText;

    private TextInputLayout emailLogInLayout;
    private TextInputLayout passwordLogInLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in_activity);

        user = (User) getIntent().getSerializableExtra(userId);

        LoginButton = (MaterialButton) findViewById(R.id.signup_button);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean userLogged = searchUser();

                if (userLogged) {
                    Intent intent = new Intent(LoginScreen.this, HomeScreen.class);
                    intent.putExtra(userId, user);
                    startActivity(intent);
                    finish();
                }
            }
        });

        SignUpOptButton = (MaterialButton) findViewById(R.id.login_button);

        SignUpOptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginScreen.this, SignupScreen.class);
                intent.putExtra(userId, user);
                startActivity(intent);
            }
        });

        emailLogInText = (TextInputEditText) findViewById(R.id.name_login);
        passwordLogInText = (TextInputEditText) findViewById(R.id.password_login);

        emailLogInLayout = (TextInputLayout) findViewById(R.id.name_login_layout);
        passwordLogInLayout = (TextInputLayout) findViewById(R.id.password_login_layout);

        emailLogInText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                emailLogInLayout.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordLogInText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                passwordLogInLayout.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean searchUser(){
        try {
            user.logInUser(emailLogInText.getText().toString(), passwordLogInText.getText().toString());

            return true;

        } catch (UserEmailNullException e) {
            emailLogInLayout.setError("The email is empty");
        } catch (UserPasswordNullException e) {
            passwordLogInLayout.setError("The password is empty");
        } catch (UserPasswordLowSecurityException e){
            passwordLogInLayout.setError("The password has to be minimum of 8 characters long");
        } catch (UserIncorrectCredentialsException e){
            emailLogInLayout.setError("Incorrect credentials");
        } catch (UserException ignored){}

        return false;
    }
}
