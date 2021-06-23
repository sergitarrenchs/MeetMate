package com.lasalle.meet;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.appcompat.app.AppCompatActivity;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.lasalle.meet.enities.User;
import com.lasalle.meet.exceptions.userexceptions.UserUnableDeletionException;

public class ProfileScreen extends AppCompatActivity {
    private MaterialButton LogOutButton;
    private MaterialButton DeleteAccButton;

    private FloatingActionButton editButton;
    private boolean infoEditable = false;

    private TextInputEditText fullNameText;
    private TextInputEditText emailText;
    private TextInputEditText passwordText;
    private TextInputEditText usernameText;

    private float x1,x2,y1,y2;

    private User user;
    private static String userId = "USER_ID";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        user = (User) getIntent().getSerializableExtra(userId);

        LogOutButton = (MaterialButton) findViewById(R.id.logout_button);

        LogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.logOutUser();

                Intent intent = new Intent(ProfileScreen.this, LoginScreen.class);
                intent.putExtra(userId, user);
                startActivity(intent);
            }
        });

        DeleteAccButton = (MaterialButton) findViewById(R.id.delete_account_button);

        DeleteAccButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    user.deleteUser();

                    Intent intent = new Intent(ProfileScreen.this, LoginScreen.class);
                    intent.putExtra(userId, user);
                    startActivity(intent);

                } catch (UserUnableDeletionException ignored) {}
            }
        });

        fullNameText = (TextInputEditText) findViewById(R.id.edit_profile_name);
        fullNameText.setText(user.getFullName());
        fullNameText.setEnabled(false);

        emailText = (TextInputEditText) findViewById(R.id.profile_email);
        emailText.setText(user.getEmail());
        emailText.setEnabled(false);

        passwordText = (TextInputEditText) findViewById(R.id.profile_password);
        passwordText.setText(user.getPassword());
        passwordText.setEnabled(false);


        usernameText = (TextInputEditText) findViewById(R.id.progile_username);
        usernameText.setText(user.getUsername());
        usernameText.setEnabled(false);

        editButton = (FloatingActionButton) findViewById(R.id.editProfileInfoButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonShowPopupWindowClick(v);
            }
        });

    }

    public void onButtonShowPopupWindowClick(View view) {
        MaterialButton CancelButton;
        MaterialButton SaveButton;
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.profile_popup_activity, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        CancelButton = (MaterialButton) popupView.findViewById(R.id.cancel_button);

        // dismiss the popup window when touched
        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        SaveButton = (MaterialButton) popupView.findViewById(R.id.save_button);

        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    public boolean onTouchEvent(MotionEvent touchEvent){
        switch(touchEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();
                if(x1 < x2){
                    Intent i = new Intent(ProfileScreen.this, ChatSelectorScreen.class);
                    i.putExtra(userId, user);
                    startActivity(i);
                }else if(x1 > x2){
                    Intent i = new Intent(ProfileScreen.this, HomeScreen.class);
                    i.putExtra(userId, user);
                    startActivity(i);
                }
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        user.logOutUser();

        Intent intent = new Intent(ProfileScreen.this, HomeScreen.class);
        intent.putExtra(userId, user);
        startActivity(intent);
    }
}
