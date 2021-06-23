package com.lasalle.meet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lasalle.meet.apiadapter.EventAdapterView;
import com.lasalle.meet.enities.APIAdapter;
import com.lasalle.meet.enities.Event;
import com.google.android.material.button.MaterialButton;
import com.lasalle.meet.enities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;


public class EventTimeline extends AppCompatActivity implements EventAdapterView.OnNoteListener {

    private User user;
    private static String userId = "USER_ID";
    private static String eventId = "EVENT_ID";

    private List<Event> eventList;
    private RecyclerView recyclerView;

    private MaterialButton allEventsButton;
    private MaterialButton yoursEventsButton;
    private MaterialButton othersEventsButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline_activity);

        user = (User) getIntent().getSerializableExtra(userId);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_event);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        getAllEvents("");

        EventAdapterView eventAdapterView = new EventAdapterView(eventList, this);
        recyclerView.setAdapter(eventAdapterView);


        allEventsButton = (MaterialButton) findViewById(R.id.all_events_button);
        allEventsButton.setBackgroundColor(getResources().getColor(R.color.white));

        allEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allEventsButton.setBackgroundColor(getResources().getColor(R.color.white));
                yoursEventsButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                othersEventsButton.setBackgroundColor(getResources().getColor(R.color.transparent));

                getAllEvents("");
                EventAdapterView eventAdapterView = new EventAdapterView(eventList, EventTimeline.this::onNoteClick);
                recyclerView.setAdapter(eventAdapterView);
            }
        });

        yoursEventsButton = (MaterialButton) findViewById(R.id.yours_events_button);
        yoursEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allEventsButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                yoursEventsButton.setBackgroundColor(getResources().getColor(R.color.white));
                othersEventsButton.setBackgroundColor(getResources().getColor(R.color.transparent));

                getYourEvents();
                EventAdapterView eventAdapterView = new EventAdapterView(eventList, EventTimeline.this::onNoteClick);
                recyclerView.setAdapter(eventAdapterView);

            }
        });

        othersEventsButton = (MaterialButton) findViewById(R.id.others_events_button);
        othersEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allEventsButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                yoursEventsButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                othersEventsButton.setBackgroundColor(getResources().getColor(R.color.white));

                getOtherEvents();
                EventAdapterView eventAdapterView = new EventAdapterView(eventList, EventTimeline.this::onNoteClick);
                recyclerView.setAdapter(eventAdapterView);

            }
        });

        //Initial start
        allEventsButton.setBackgroundColor(getResources().getColor(R.color.white));
        yoursEventsButton.setBackgroundColor(getResources().getColor(R.color.transparent));
        othersEventsButton.setBackgroundColor(getResources().getColor(R.color.transparent));

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EventTimeline.this, HomeScreen.class);
        intent.putExtra(userId, user);
        startActivity(intent);
    }

    private void getAllEvents(String keyValue) {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        Call<List<Event>> call = APIAdapter.getApiService().getEvent(keyValue,"Bearer " + user.getAccessToken());

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

    private void getOtherEvents() {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        Call<List<Event>> call = APIAdapter.getApiService().getEvent("","Bearer " + user.getAccessToken());

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

            List<Event> eventOthers = new ArrayList<Event>();

            for (int i = 0; i < eventList.size(); i++) {
                if (eventList.get(i).getOwner_id() != user.getId()){
                    eventOthers.add(eventList.get(i));
                }
            }

            eventList = eventOthers;

        } catch (InterruptedException e) {
            //TODO: Throw Exception Event Incorrect Error
        }
    }

    @Override
    public void onNoteClick(int position) {
        Toast.makeText(this, "You have clicked: " + eventList.get(position).getName(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, EventView.class);
        intent.putExtra(userId, user);
        intent.putExtra(eventId, eventList.get(position));
        startActivity(intent);
    }
}
