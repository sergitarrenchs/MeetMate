package com.lasalle.meet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.lasalle.meet.apiadapter.FriendAdapterView;
import com.lasalle.meet.apiadapter.MessageAdapterView;
import com.lasalle.meet.enities.APIAdapter;
import com.lasalle.meet.enities.Message;
import com.lasalle.meet.enities.User;
import com.lasalle.meet.exceptions.messageexceptions.MessageNullException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatScreen extends AppCompatActivity {
    private static final String TAG = "ChatScreen";
    private float x1,x2,y1,y2;

    private User user;
    private static String userId = "USER_ID";

    private User otherUser;
    private static String userMessageId = "USER_MESSAGE_ID";

    private MaterialTextView userName;
    private MaterialTextView welcomeMessage;

    private LocalDateTime currentDate;

    private TextInputEditText message;
    private FloatingActionButton sendButton;
    private RecyclerView recyclerView;
    private MessageAdapterView messageAdapterView;

    private List<Message> messageList = new ArrayList<Message>();

    private Timer timer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        user = (User) getIntent().getSerializableExtra(userId);

        otherUser = (User) getIntent().getSerializableExtra(userMessageId);

        userName = (MaterialTextView) findViewById(R.id.edit_profile_name);

        currentDate = LocalDateTime.ofInstant(new Date().toInstant(),
                ZoneId.systemDefault());

        welcomeMessage = (MaterialTextView) findViewById(R.id.welcomeMessage);

        userName.setText(otherUser.getFullName());

        if (currentDate.getHour() >= 23 && currentDate.getHour() <= 5) {
            welcomeMessage.setText("Good Night");
        } else if (currentDate.getHour() >= 20 && currentDate.getHour() <= 22) {
            welcomeMessage.setText("Good Evening");
        } else if (currentDate.getHour() >= 15 && currentDate.getHour() <= 19) {
            welcomeMessage.setText("Good Afternoon");
        } else if (currentDate.getHour() >= 5 && currentDate.getHour() <= 14) {
            welcomeMessage.setText("Good Morning");
        }

        message = (TextInputEditText) findViewById(R.id.send_message_input);

        sendButton = (FloatingActionButton) findViewById(R.id.searchFloatingActionButton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageToSend = message.getText().toString();
                message.setText("");
                try {
                    user.sendMessage(otherUser.getId(), messageToSend);

                    getMessageBetweenUsers();

                    messageAdapterView.setDataSet(messageList);

                } catch (MessageNullException e) {
                    Log.w(TAG, "onClick: Do not send empty messages");
                }
            }
        });

        getMessageBetweenUsers();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_chat_message);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        messageAdapterView = new MessageAdapterView(messageList, user);
        recyclerView.setAdapter(messageAdapterView);

        configureMessageRate();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ChatScreen.this, ChatSelectorScreen.class);
        intent.putExtra(userId, user);
        startActivity(intent);
    }

    public void getMessageBetweenUsers() {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        Call<List<Message>> call = APIAdapter.getApiService().getUserIDMessages(otherUser.getId(),"Bearer " + user.getAccessToken());

        call.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                messageList = response.body();
                countDownLatch.countDown();
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                countDownLatch.countDown();
            }
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            //TODO: FIX
        }
    }

    public void configureMessageRate() {
        timer = new Timer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run() {
                getMessageBetweenUsers();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateRecyclerView();
                    }
                });
            }
        }, 100, 50);
    }

    private void updateRecyclerView() {
        messageAdapterView.setDataSet(messageList);
    }


}
