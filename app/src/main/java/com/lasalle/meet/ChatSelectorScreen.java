package com.lasalle.meet;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.lasalle.meet.apiadapter.EventAdapterView;
import com.lasalle.meet.apiadapter.FriendAdapterView;
import com.lasalle.meet.enities.APIAdapter;
import com.lasalle.meet.enities.User;
import com.lasalle.meet.exceptions.userexceptions.UserIncorrectCredentialsException;
import com.lasalle.meet.exceptions.userexceptions.UserUnableDeletionException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatSelectorScreen extends AppCompatActivity implements FriendAdapterView.OnNoteListener{
    private float x1,x2,y1,y2;

    private User user;
    private static String userId = "USER_ID";
    private static String userMessageId = "USER_MESSAGE_ID";

    private FloatingActionButton searchButton;
    private MaterialTextView userName;
    private MaterialTextView welcomeMessage;

    private LocalDateTime currentDate;

    private List<User> friendList = new ArrayList<User>();;
    private List<User> userList = new ArrayList<User>();
    private RecyclerView recyclerView;

    private TextInputEditText searchUser;

    private int userError;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_selector_activity);

        user = (User) getIntent().getSerializableExtra(userId);

        currentDate = LocalDateTime.ofInstant(new Date().toInstant(),
                ZoneId.systemDefault());

        welcomeMessage = (MaterialTextView) findViewById(R.id.welcomeMessage);

        recyclerView = (RecyclerView) findViewById(R.id.scrollView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        if (currentDate.getHour() >= 23 && currentDate.getHour() <= 5) {
            welcomeMessage.setText("Good Night");
        } else if (currentDate.getHour() >= 20 && currentDate.getHour() <= 22) {
            welcomeMessage.setText("Good Evening");
        } else if (currentDate.getHour() >= 15 && currentDate.getHour() <= 19) {
            welcomeMessage.setText("Good Afternoon");
        } else if (currentDate.getHour() >= 5 && currentDate.getHour() <= 14) {
            welcomeMessage.setText("Good Morning");
        }

        searchButton = (FloatingActionButton) findViewById(R.id.searchFloatingActionButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatSelectorScreen.this, AddFriend.class);
                intent.putExtra(userId, user);
                startActivity(intent);
            }
        });


        friendList = user.getFriends();

        userList.clear();
        userList.addAll(friendList);

        FriendAdapterView friendAdapterView = new FriendAdapterView(userList, ChatSelectorScreen.this::onNoteClick, friendList);
        recyclerView.setAdapter(friendAdapterView);

        searchUser = (TextInputEditText) findViewById(R.id.search_text_user);

        searchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    searchUsers(s.toString());

                    FriendAdapterView friendAdapterView = new FriendAdapterView(userList, ChatSelectorScreen.this::onNoteClick, friendList);
                    recyclerView.setAdapter(friendAdapterView);

                } catch (UserIncorrectCredentialsException e) {
                    Toast.makeText(ChatSelectorScreen.this,"There are no users with that name",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().equals("")) {
                    //TODO: Show again Friends
                    userList.clear();
                    userList.addAll(friendList);

                    FriendAdapterView friendAdapterView = new FriendAdapterView(userList, ChatSelectorScreen.this::onNoteClick, friendList);
                    recyclerView.setAdapter(friendAdapterView);
                }

            }
        });

    }

    private void searchUsers(String stringToSearch) throws UserIncorrectCredentialsException {
        CountDownLatch countDownLatch = new CountDownLatch(1);


        Call<List<User>> call = APIAdapter.getApiService().searchUser(stringToSearch,"Bearer " + user.getAccessToken());

        userError = 0;

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(!response.isSuccessful()) {
                    userError = 404;

                }else {
                    List<User> temporalList = response.body();
                    userList.clear();

                    for (int i = 0; i < temporalList.size(); i++) {
                        if (temporalList.get(i).getId() != user.getId()) {
                            userList.add(temporalList.get(i));
                        }
                    }
                }
                countDownLatch.countDown();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                countDownLatch.countDown();
                userError = 404;
            }

        });

        try {
            countDownLatch.await();
            if (userError == 404) {
                throw new UserIncorrectCredentialsException();
            }
        } catch (InterruptedException e) {
            throw new UserIncorrectCredentialsException();
        }

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
                    Intent i = new Intent(ChatSelectorScreen.this, HomeScreen.class);
                    i.putExtra(userId, user);
                    startActivity(i);
                }else if(x1 > x2){
                    Intent i = new Intent(ChatSelectorScreen.this, ProfileScreen.class);
                    i.putExtra(userId, user);
                    startActivity(i);
                }
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(ChatSelectorScreen.this, HomeScreen.class);
        intent.putExtra(userId, user);
        startActivity(intent);
    }

    @Override
    public void onNoteClick(int position) {
        Toast.makeText(this, "You have clicked: " + userList.get(position).getName(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, ChatScreen.class);
        intent.putExtra(userId, user);
        intent.putExtra(userMessageId, userList.get(position));
        startActivity(intent);
    }


}
