package com.lasalle.meet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lasalle.meet.apiadapter.AddNewFriendAdapterView;
import com.lasalle.meet.apiadapter.FriendAdapterView;
import com.lasalle.meet.enities.User;

import java.util.ArrayList;
import java.util.List;

public class AddFriend extends AppCompatActivity implements AddNewFriendAdapterView.OnNoteListener {
    private List<User> friendList = new ArrayList<User>();

    private User user;
    private static String userId = "USER_ID";
    private AddNewFriendAdapterView addNewFriendAdapterView;
    private RecyclerView recyclerView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_request);

        user = (User) getIntent().getSerializableExtra(userId);

        for (int i = 0; i < 20; i++) {
            friendList.add(new User("Hi " + (i+1), "HOW", "AH", "UT", "ssss"));
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
        Toast.makeText(this, "You have added: " + friendList.get(position).getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDiscardClick(int position) {
        Toast.makeText(this, "You have discarded: " + friendList.get(position).getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(AddFriend.this, ChatSelectorScreen.class);
        intent.putExtra(userId, user);
        startActivity(intent);
    }
}
