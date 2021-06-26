package com.lasalle.meet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lasalle.meet.apiadapter.EventAdapterView;
import com.lasalle.meet.enities.APIAdapter;
import com.lasalle.meet.enities.Event;
import com.google.android.material.button.MaterialButton;
import com.lasalle.meet.enities.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
    private MaterialButton subAllEventsButton;
    private MaterialButton yoursEventsButton;
    private MaterialButton othersEventsButton;
    private MaterialButton currentEventsButton;
    private MaterialButton finishedEventsButton;
    private MaterialButton futureEventsButton;

    private ImageView leftTimelineSelect;
    private ImageView centerTimelineSelect;
    private ImageView rightTimelineSelect;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline_activity);

        user = (User) getIntent().getSerializableExtra(userId);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_event);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        getAllEvents("");

        EventAdapterView eventAdapterView = new EventAdapterView(eventList, this);
        recyclerView.setAdapter(eventAdapterView);


        leftTimelineSelect = (ImageView) findViewById(R.id.left_timeline_select);
        leftTimelineSelect.setVisibility(View.VISIBLE);

        allEventsButton = (MaterialButton) findViewById(R.id.all_events_button);
        allEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allEventsButton.setBackgroundColor(getResources().getColor(R.color.white));
                yoursEventsButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                othersEventsButton.setBackgroundColor(getResources().getColor(R.color.transparent));

                leftTimelineSelect.setVisibility(View.VISIBLE);
                centerTimelineSelect.setVisibility(View.INVISIBLE);
                rightTimelineSelect.setVisibility(View.INVISIBLE);

                getAllEvents("");
                EventAdapterView eventAdapterView = new EventAdapterView(eventList, EventTimeline.this::onNoteClick);
                recyclerView.setAdapter(eventAdapterView);
            }
        });

        centerTimelineSelect = (ImageView) findViewById(R.id.center_timeline_select);

        yoursEventsButton = (MaterialButton) findViewById(R.id.yours_events_button);
        yoursEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allEventsButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                yoursEventsButton.setBackgroundColor(getResources().getColor(R.color.white));
                othersEventsButton.setBackgroundColor(getResources().getColor(R.color.transparent));

                leftTimelineSelect.setVisibility(View.INVISIBLE);
                centerTimelineSelect.setVisibility(View.VISIBLE);
                rightTimelineSelect.setVisibility(View.INVISIBLE);

                getYourEvents();
                EventAdapterView eventAdapterView = new EventAdapterView(eventList, EventTimeline.this::onNoteClick);
                recyclerView.setAdapter(eventAdapterView);

            }
        });

        rightTimelineSelect = (ImageView) findViewById(R.id.right_timeline_select);

        othersEventsButton = (MaterialButton) findViewById(R.id.others_events_button);
        othersEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allEventsButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                yoursEventsButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                othersEventsButton.setBackgroundColor(getResources().getColor(R.color.white));

                leftTimelineSelect.setVisibility(View.INVISIBLE);
                centerTimelineSelect.setVisibility(View.INVISIBLE);
                rightTimelineSelect.setVisibility(View.VISIBLE);


                getOtherEvents();
                EventAdapterView eventAdapterView = new EventAdapterView(eventList, EventTimeline.this::onNoteClick);
                recyclerView.setAdapter(eventAdapterView);

            }
        });

        subAllEventsButton = (MaterialButton) findViewById(R.id.sub_all_events_button);
        subAllEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subAllEventsButton.setBackgroundColor(getResources().getColor(R.color.white));
                currentEventsButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                finishedEventsButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                futureEventsButton.setBackgroundColor(getResources().getColor(R.color.transparent));

                getAllEvents("");
                EventAdapterView eventAdapterView = new EventAdapterView(eventList, EventTimeline.this::onNoteClick);
                recyclerView.setAdapter(eventAdapterView);
            }
        });


        currentEventsButton = (MaterialButton) findViewById(R.id.current_type_button);
        currentEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subAllEventsButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                currentEventsButton.setBackgroundColor(getResources().getColor(R.color.white));
                finishedEventsButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                futureEventsButton.setBackgroundColor(getResources().getColor(R.color.transparent));


                getOtherEvents();
                EventAdapterView eventAdapterView = new EventAdapterView(eventList, EventTimeline.this::onNoteClick);
                recyclerView.setAdapter(eventAdapterView);

            }
        });

        finishedEventsButton = (MaterialButton) findViewById(R.id.finished_type_button);
        finishedEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subAllEventsButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                currentEventsButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                finishedEventsButton.setBackgroundColor(getResources().getColor(R.color.white));
                futureEventsButton.setBackgroundColor(getResources().getColor(R.color.transparent));

                getOtherEvents();
                EventAdapterView eventAdapterView = new EventAdapterView(eventList, EventTimeline.this::onNoteClick);
                recyclerView.setAdapter(eventAdapterView);

            }
        });

        futureEventsButton = (MaterialButton) findViewById(R.id.future_type_button);
        futureEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subAllEventsButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                currentEventsButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                finishedEventsButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                futureEventsButton.setBackgroundColor(getResources().getColor(R.color.white));

                getOtherEvents();
                EventAdapterView eventAdapterView = new EventAdapterView(eventList, EventTimeline.this::onNoteClick);
                recyclerView.setAdapter(eventAdapterView);

            }
        });


        //Initial start
        allEventsButton.setBackgroundColor(getResources().getColor(R.color.white));
        subAllEventsButton.setBackgroundColor(getResources().getColor(R.color.white));
        yoursEventsButton.setBackgroundColor(getResources().getColor(R.color.transparent));
        othersEventsButton.setBackgroundColor(getResources().getColor(R.color.transparent));
        currentEventsButton.setBackgroundColor(getResources().getColor(R.color.transparent));
        finishedEventsButton.setBackgroundColor(getResources().getColor(R.color.transparent));
        futureEventsButton.setBackgroundColor(getResources().getColor(R.color.transparent));

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

            Collections.sort(eventList, new Comparator<Event>() {
                @Override
                public int compare(Event o1, Event o2) {
                    Date first = o1.getEndDateObj();
                    Date second = o2.getEndDateObj();

                    if (first == null && second != null) {
                        return 1;
                    } else if (first != null && second == null) {
                        return -1;
                    } else if (first == null && second == null) {
                        return 0;
                    } else {
                        long diff = first.getTime() - second.getTime();

                        if (diff == 0) {
                            return 0;
                        }

                        return (int) (diff/Math.abs(diff));
                    }
                }
            });

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

            Collections.sort(eventList, new Comparator<Event>() {
                @Override
                public int compare(Event o1, Event o2) {
                    Date first = o1.getEndDateObj();
                    Date second = o2.getEndDateObj();

                    if (first == null && second != null) {
                        return 1;
                    } else if (first != null && second == null) {
                        return -1;
                    } else if (first == null && second == null) {
                        return 0;
                    } else {
                        long diff = first.getTime() - second.getTime();

                        if (diff == 0) {
                            return 0;
                        }

                        return (int) (diff/Math.abs(diff));
                    }
                }
            });

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

            Collections.sort(eventList, new Comparator<Event>() {
                @Override
                public int compare(Event o1, Event o2) {
                    Date first = o1.getEndDateObj();
                    Date second = o2.getEndDateObj();

                    if (first == null && second != null) {
                        return 1;
                    } else if (first != null && second == null) {
                        return -1;
                    } else if (first == null && second == null) {
                        return 0;
                    } else {
                        long diff = first.getTime() - second.getTime();

                        if (diff == 0) {
                            return 0;
                        }

                        return (int) (diff/Math.abs(diff));
                    }
                }
            });

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
