package com.lasalle.meet;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.lasalle.meet.enities.APIAdapter;
import com.lasalle.meet.enities.Event;
import com.lasalle.meet.enities.User;
import com.lasalle.meet.exceptions.eventexceptions.EventInvalidCredentialsException;
import com.lasalle.meet.exceptions.userexceptions.UserIncorrectCredentialsException;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventView extends AppCompatActivity {

    private static final String TAG = "EventView";
    private User user;
    private static String userId = "USER_ID";
    private Event event;
    private static String eventId = "EVENT_ID";

    private MaterialTextView eventName;
    private MaterialTextView eventAddress;
    private MaterialTextView eventType;
    private MaterialTextView eventDate;
    private ImageView eventImage;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private MaterialButton mButton;
    private FloatingActionButton mFloatingButton;
    private MaterialTextView eventDescription;
    private MaterialTextView eventStartDate;
    private MaterialTextView eventEndDate;
    private MaterialTextView numberParticipants;

    private Address address;
    private List<Event> assistedEvents;
    private int eventError = 0;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_view_activity);

        user = (User) getIntent().getSerializableExtra(userId);
        event = (Event) getIntent().getSerializableExtra(eventId);

        eventName = (MaterialTextView) findViewById(R.id.textView2);

        eventAddress = (MaterialTextView) findViewById(R.id.textView4);

        eventType = (MaterialTextView) findViewById(R.id.textView3);

        eventDate = (MaterialTextView) findViewById(R.id.textView5);

        eventImage = (ImageView) findViewById(R.id.eventImageView);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.event_default_picture)
                .error(R.drawable.event_default_picture);


        Glide.with(this).load(event.getImage()).apply(options).into(eventImage);

        eventDescription = (MaterialTextView) findViewById(R.id.event_description);

        eventStartDate = (MaterialTextView) findViewById(R.id.event_description3);

        eventEndDate = (MaterialTextView) findViewById(R.id.event_description5);

        numberParticipants = (MaterialTextView) findViewById(R.id.event_description7);

        eventName.setText(event.getName());

        eventAddress.setText(event.getLocation());

        eventType.setText(event.getType());

        eventDate.setText(event.getDate());

        eventDescription.setText(event.getDescription());

        eventStartDate.setText(event.getEventStart_date());

        eventEndDate.setText(event.getEventEnd_date());

        numberParticipants.setText(String.valueOf(event.getN_participators()));

        eventImage.setImageDrawable(getDrawable(R.drawable.event_default_picture));

        mButton = (MaterialButton) findViewById(R.id.join_button);

        mFloatingButton = (FloatingActionButton) findViewById(R.id.editProfileInfoButton);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (event.getOwner_id() != user.getId() && !containsEvent()) {
                    try {
                        event.eventAssist(user.getAccessToken());
                    } catch (EventInvalidCredentialsException e) {
                        Toast.makeText(EventView.this, "There has been an error joining", Toast.LENGTH_SHORT).show();
                    }
                } else if (event.getOwner_id() != user.getId() && containsEvent()){
                    Toast.makeText(EventView.this, "You have already joined this event", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        event.deleteEvent(user.getAccessToken());
                        onBackPressed();
                    } catch (UserIncorrectCredentialsException e) {
                        Log.e(TAG, "onClick: Couldn't connect to API to delete the user");
                    }
                }
            }
        });

        mFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (event.getOwner_id() == user.getId()) {
                    Intent intent = new Intent(EventView.this, EventModify.class);
                    intent.putExtra(userId, user);
                    intent.putExtra(eventId, event);
                    startActivity(intent);
                }
            }
        });

        if (event.getOwner_id() == user.getId()) {
            mButton.setText(R.string.delete);
            mFloatingButton.setVisibility(View.VISIBLE);
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView2);
        mapFragment.getMapAsync(this::onMapReady);

        getLocation();


    }

    private boolean containsEvent() {
        getAssistedEvents();

        for (int i = 0; i < assistedEvents.size(); i++) {
            if (assistedEvents.get(i).getID() == event.getID()) {
                return true;
            }
        }

        return false;
    }

    private void getAssistedEvents() {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        Call<List<Event>> call = APIAdapter.getApiService().getUserAssistances(user.getId(),"Bearer " + user.getAccessToken());

        call.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                if (!response.isSuccessful()) {
                    eventError = response.code();
                }else {
                    assistedEvents = response.body();
                }
                countDownLatch.countDown();
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                countDownLatch.countDown();
            }
        });

        try {
            countDownLatch.await();
            if (eventError == 400) {
                assistedEvents.clear();
            }

        } catch (InterruptedException e) {
            assistedEvents.clear();
        }
    }

    private void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);

        final Task location = fusedLocationProviderClient.getLastLocation();
        location.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull @NotNull Task task) {
                if (task.isSuccessful()){
                    Location currentLocation = (Location) task.getResult();

                    if (address == null && currentLocation != null) {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),15));
                    }

                }else {
                    Toast.makeText(EventView.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                }
            }
        });

        try {
        List<Address> addresses;

            addresses = new Geocoder(EventView.this).getFromLocationName(event.getLocation(),5);

            if (addresses.size() > 0) {
                address = addresses.get(0);

                mMap.addMarker(new MarkerOptions().position(new LatLng(address.getLatitude(), address.getLongitude())));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(address.getLatitude(), address.getLongitude()),15));
            }

        } catch (IOException e) {
            Log.e(TAG, "Could not found address");

        }
    }

    private void getLocation() {
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (permission == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){

            }else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EventView.this, EventTimeline.class);
        intent.putExtra(userId, user);
        startActivity(intent);
    }
}
