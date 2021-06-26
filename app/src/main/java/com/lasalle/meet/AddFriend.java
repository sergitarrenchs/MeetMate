package com.lasalle.meet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lasalle.meet.apiadapter.AddNewFriendAdapterView;
import com.lasalle.meet.apiadapter.FriendAdapterView;
import com.lasalle.meet.enities.APIAdapter;
import com.lasalle.meet.enities.User;
import com.lasalle.meet.exceptions.userexceptions.UserIncorrectCredentialsException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFriend extends AppCompatActivity implements AddNewFriendAdapterView.OnNoteListener {
    private List<User> friendList = new ArrayList<User>();

    private User user;
    private static String userId = "USER_ID";
    private AddNewFriendAdapterView addNewFriendAdapterView;
    private RecyclerView recyclerView;

    private int userError;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_request);

        user = (User) getIntent().getSerializableExtra(userId);

        try {
            requestFriends();
        } catch (UserIncorrectCredentialsException e) {
            friendList = new ArrayList<User>();
        }

        recyclerView = (RecyclerView) findViewById(R.id.scrollView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        addNewFriendAdapterView = new AddNewFriendAdapterView(friendList, AddFriend.this);
        recyclerView.setAdapter(addNewFriendAdapterView);

    }

    @Override
    public void onNoteClick(int position) {
        Toast.makeText(this, "You have clicked: " + friendList.get(position).getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddClick(int position) {

        try {
            addFriends(friendList.get(position).getId());
            Toast.makeText(this, "You have added: " + friendList.get(position).getName(), Toast.LENGTH_SHORT).show();

            try {
                requestFriends();
            } catch (UserIncorrectCredentialsException e) {
                friendList = new ArrayList<User>();
            }

            addNewFriendAdapterView.setDataSet(friendList);

        } catch (UserIncorrectCredentialsException e) {
            Toast.makeText(this, "Couldn't add a friend: " + friendList.get(position).getName(), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onDiscardClick(int position) {
        try {
            discardFriends(friendList.get(position).getId());
            Toast.makeText(this, "You have discarded: " + friendList.get(position).getName(), Toast.LENGTH_SHORT).show();

            try {
                requestFriends();
            } catch (UserIncorrectCredentialsException e) {
                friendList = new ArrayList<User>();
            }

            addNewFriendAdapterView.setDataSet(friendList);

        } catch (UserIncorrectCredentialsException e) {
            Toast.makeText(this, "Couldn't discard a friend: " + friendList.get(position).getName(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(AddFriend.this, ChatSelectorScreen.class);
        intent.putExtra(userId, user);
        startActivity(intent);
    }

    public void requestFriends () throws UserIncorrectCredentialsException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        userError = 0;

        Call<List<User>> call = APIAdapter.getApiService().getFriendRequests("Bearer " + user.getAccessToken());

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!response.isSuccessful()) {
                    userError = response.code();
                } else {
                    friendList = response.body();
                }

                countDownLatch.countDown();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                countDownLatch.countDown();
            }
        });

        try {
            countDownLatch.await();

            if (userError == 400) {
                throw new UserIncorrectCredentialsException();
            }

        } catch (InterruptedException e) {
            throw new UserIncorrectCredentialsException();
        }
    }

    public void addFriends (int friendID) throws UserIncorrectCredentialsException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        userError = 0;

        Call<User> call = APIAdapter.getApiService().putFriendRequestAccept(friendID,"Bearer " + user.getAccessToken());

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    userError = response.code();
                }

                countDownLatch.countDown();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                countDownLatch.countDown();
            }
        });

        try {
            countDownLatch.await();

            if (userError == 400) {
                throw new UserIncorrectCredentialsException();
            }

        } catch (InterruptedException e) {
            throw new UserIncorrectCredentialsException();
        }
    }

    public void discardFriends (int friendID) throws UserIncorrectCredentialsException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        userError = 0;

        Call<User> call = APIAdapter.getApiService().deleteFriendRequest(friendID,"Bearer " + user.getAccessToken());

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    userError = response.code();
                }

                countDownLatch.countDown();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                countDownLatch.countDown();
            }
        });

        try {
            countDownLatch.await();

            if (userError == 400) {
                throw new UserIncorrectCredentialsException();
            }

        } catch (InterruptedException e) {
            throw new UserIncorrectCredentialsException();
        }
    }
}
