package com.lasalle.meet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.appcompat.app.AppCompatActivity;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.lasalle.meet.enities.APIAdapter;
import com.lasalle.meet.enities.Event;
import com.lasalle.meet.enities.User;
import com.lasalle.meet.exceptions.userexceptions.UserEmailExistException;
import com.lasalle.meet.exceptions.userexceptions.UserEmailNullException;
import com.lasalle.meet.exceptions.userexceptions.UserException;
import com.lasalle.meet.exceptions.userexceptions.UserPasswordLowSecurityException;
import com.lasalle.meet.exceptions.userexceptions.UserPasswordNotEqualException;
import com.lasalle.meet.exceptions.userexceptions.UserPasswordNullException;
import com.lasalle.meet.exceptions.userexceptions.UserUnableDeletionException;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileScreen extends AppCompatActivity {
    private MaterialButton LogOutButton;
    private MaterialButton DeleteAccButton;

    private FloatingActionButton editButton;
    private boolean infoEditable = false;

    private TextInputEditText fullNameText;
    private TextInputEditText emailText;
    private TextInputEditText passwordText;
    private TextInputEditText usernameText;

    private MaterialButton CancelButton;
    private MaterialButton SaveButton;

    private TextInputEditText nameField = null;
    private TextInputEditText lastNameField = null;
    private TextInputEditText emailField = null;
    private TextInputEditText passwordField = null;

    private MaterialTextView createdEvents;
    private MaterialTextView assistedEvents;

    private float x1,x2,y1,y2;

    private User user;
    private static String userId = "USER_ID";
    private List<Event> eventList;
    private List<Event> assistedList;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        user = (User) getIntent().getSerializableExtra(userId);

        getYourEvents();

        getYourAssistences();

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

        createdEvents = (MaterialTextView) findViewById(R.id.youhave_textView5);
        createdEvents.setText(String.valueOf(eventList.size()));

        assistedEvents = (MaterialTextView) findViewById(R.id.youhave_textView7);
        assistedEvents.setText(String.valueOf(assistedList.size()));

    }

    public void onButtonShowPopupWindowClick(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.profile_popup_activity, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        nameField = (TextInputEditText) popupView.findViewById(R.id.edit_profile_name);
        nameField.setText(user.getName());

        lastNameField = (TextInputEditText) popupView.findViewById(R.id.edit_profile_surename);
        lastNameField.setText(user.getLast_name());

        emailField = (TextInputEditText) popupView.findViewById(R.id.edit_profile_email);
        emailField.setText(user.getEmail());

        passwordField = (TextInputEditText) popupView.findViewById(R.id.edit_profile_password);
        passwordField.setText(user.getPassword());

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
                updateUser();

                fullNameText.setText(user.getFullName());

                emailText.setText(user.getEmail());

                passwordText.setText(user.getPassword());

                usernameText.setText(user.getUsername());
                popupWindow.dismiss();
            }

            private void updateUser() {
                try {
                    user.update(nameField.getText().toString(), lastNameField.getText().toString(), emailField.getText().toString(), passwordField.getText().toString());
                } catch (UserEmailNullException e) {
                    emailField.setError("The email is empty");
                } catch (UserPasswordNullException e) {
                    passwordField.setError("The password is empty");
                } catch (UserPasswordLowSecurityException e){
                    passwordField.setError("The password has to be minimum of 8 characters long");
                } catch (UserEmailExistException e){
                    emailField.setError("The email already exist");
                } catch (UserException ignored){}
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

    private void getYourEvents() {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        Call<List<Event>> call = APIAdapter.getApiService().getUserEvent(user.getId(),"Bearer " + user.getAccessToken());

        call.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                if (response.isSuccessful()){
                    eventList = response.body();
                    countDownLatch.countDown();
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                countDownLatch.countDown();
            }
        });

        try {
            countDownLatch.await();

        } catch (InterruptedException e) {
            //TODO: Throw Exception Event Incorrect Error
        }
    }

    private void getYourAssistences() {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        Call<List<Event>> call = APIAdapter.getApiService().getUserAssistances(user.getId(),"Bearer " + user.getAccessToken());

        call.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                if (response.isSuccessful()){
                    assistedList = response.body();
                    countDownLatch.countDown();
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                countDownLatch.countDown();
            }
        });

        try {
            countDownLatch.await();

        } catch (InterruptedException e) {
            //TODO: Throw Exception Event Incorrect Error
        }
    }

    @Override
    public void onBackPressed() {
        user.logOutUser();

        Intent intent = new Intent(ProfileScreen.this, HomeScreen.class);
        intent.putExtra(userId, user);
        startActivity(intent);
    }
}
