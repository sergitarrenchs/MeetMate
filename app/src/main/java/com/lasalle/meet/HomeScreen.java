package com.lasalle.meet;
//MAIN COMENT

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.lasalle.meet.enities.APIAdapter;
import com.lasalle.meet.enities.Event;
import com.lasalle.meet.enities.User;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeScreen extends AppCompatActivity {
    private GoogleMap mMap;
    private float x1, x2, y1, y2;

    private FloatingActionButton newEventButton;
    private FloatingActionButton viewTimelineButton;

    private MaterialButton allTypeButton;
    private MaterialButton educationTypeButton;
    private MaterialButton gamesTypeButton;
    private MaterialButton musicTypeButton;
    private MaterialButton sportsTypeButton;
    private MaterialButton travelTypeButton;

    private MaterialTextView welcomeMessage;

    private User user;
    private static String userId = "USER_ID";

    private Date date;
    private Date dateCompareMorning;
    private Date dateCompareAfternoon;
    private Date dateCompareNight;
    public static final String inputFormat = "HH:mm";
    private SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat, Locale.GERMANY);
    private FusedLocationProviderClient fusedLocationProviderClient;
    private List<Event> eventList = null;

    private final String ALL_EVENT = "";
    private final String EDUCATION_EVENT = "Education";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.home_activity);


        user = (User) getIntent().getSerializableExtra(userId);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this::onMapReady);

        getLocation();


        newEventButton = (FloatingActionButton) findViewById(R.id.addFloatingActionButton);

        newEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreen.this, NewEvent.class);
                intent.putExtra(userId, user);
                startActivity(intent);
            }
        });

        viewTimelineButton = (FloatingActionButton) findViewById(R.id.viewFloatingActionButton);

        viewTimelineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreen.this, EventTimeline.class);
                intent.putExtra(userId, user);
                startActivity(intent);
            }
        });

        allTypeButton = (MaterialButton) findViewById(R.id.all_type_button);
        allTypeButton.setBackgroundColor(getResources().getColor(R.color.white));

        allTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allTypeButton.setBackgroundColor(getResources().getColor(R.color.white));
                educationTypeButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                gamesTypeButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                musicTypeButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                sportsTypeButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                travelTypeButton.setBackgroundColor(getResources().getColor(R.color.transparent));

                getAllEvents(ALL_EVENT);
            }
        });

        educationTypeButton = (MaterialButton) findViewById(R.id.education_type_button);

        educationTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allTypeButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                educationTypeButton.setBackgroundColor(getResources().getColor(R.color.white));
                gamesTypeButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                musicTypeButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                sportsTypeButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                travelTypeButton.setBackgroundColor(getResources().getColor(R.color.transparent));

                getAllEvents(EDUCATION_EVENT);
            }
        });

        gamesTypeButton = (MaterialButton) findViewById(R.id.games_type_button);

        gamesTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allTypeButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                educationTypeButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                gamesTypeButton.setBackgroundColor(getResources().getColor(R.color.white));
                musicTypeButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                sportsTypeButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                travelTypeButton.setBackgroundColor(getResources().getColor(R.color.transparent));
            }
        });

        musicTypeButton = (MaterialButton) findViewById(R.id.music_type_button);

        musicTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allTypeButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                educationTypeButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                gamesTypeButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                musicTypeButton.setBackgroundColor(getResources().getColor(R.color.white));
                sportsTypeButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                travelTypeButton.setBackgroundColor(getResources().getColor(R.color.transparent));
            }
        });

        sportsTypeButton = (MaterialButton) findViewById(R.id.sports_type_button);

        sportsTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allTypeButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                educationTypeButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                gamesTypeButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                musicTypeButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                sportsTypeButton.setBackgroundColor(getResources().getColor(R.color.white));
                travelTypeButton.setBackgroundColor(getResources().getColor(R.color.transparent));
            }
        });

        travelTypeButton = (MaterialButton) findViewById(R.id.travel_type_button);

        travelTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allTypeButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                educationTypeButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                gamesTypeButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                musicTypeButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                sportsTypeButton.setBackgroundColor(getResources().getColor(R.color.transparent));
                travelTypeButton.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });
    }

    private void getAllEvents(String keyValue) {
//        CountDownLatch countDownLatch = new CountDownLatch(1);
//
//        Call<List<Event>> call = APIAdapter.getApiService().getEvent(keyValue,"Bearer " + user.getAccessToken());
//
//        call.enqueue(new Callback<List<Event>>() {
//            @Override
//            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
//                if (response.isSuccessful()){
//                    eventList = response.body();
//                    countDownLatch.countDown();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Event>> call, Throwable t) {
//                countDownLatch.countDown();
//            }
//        });
//
//        try {
//            countDownLatch.await();
//
//            new Thread(() -> runOnUiThread(new Runnable() {
//
//                @Override
//                public void run() {
//                    mMap = Event.displayAllEventMarkers(eventList, mMap, HomeScreen.this);
//                }
//            })).start();
//
//        } catch (InterruptedException e) {
//            //TODO: Throw Exception Event Incorrect Error
//        }

    }

    public boolean onTouchEvent(MotionEvent touchEvent) {
        switch (touchEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();
                if (x1 < x2) {
                    Intent i = new Intent(HomeScreen.this, ProfileScreen.class);
                    i.putExtra(userId, user);
                    startActivity(i);
                } else if (x1 > x2) {
                    Intent i = new Intent(HomeScreen.this, ChatScreen.class);
                    i.putExtra(userId, user);
                    startActivity(i);
                }
                break;
        }
        return false;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    public void onMapReady(GoogleMap googleMap) {
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

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),15));
                }else {
                    Toast.makeText(HomeScreen.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                }
            }
        });

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

    private void compareDates() {
        welcomeMessage = (MaterialTextView) findViewById(R.id.welcomeMessage);

        String compareStringMorning = "6:00";
        String compareStringAfternoon = "12:00";
        String compareStringNight = "21:00";

        Calendar now = Calendar.getInstance();


        int hour = now.get(Calendar.HOUR);
        int minute = now.get(Calendar.MINUTE);

        date = parseDate(hour + ":" + minute);
        dateCompareMorning = parseDate(compareStringMorning);
        dateCompareAfternoon = parseDate(compareStringAfternoon);
        dateCompareNight = parseDate(compareStringNight);

        if (dateCompareAfternoon.before(date) && dateCompareMorning.after(date)) {
            welcomeMessage.setText(getResources().getString(R.string.welcome_message_morning));
        }
        else if(dateCompareNight.before(date) && dateCompareAfternoon.after(date)){
            welcomeMessage.setText(getResources().getString(R.string.welcome_message_afternoon));
        }
        else{
            welcomeMessage.setText(getResources().getString(R.string.welcome_message_night));
        }
    }

    private Date parseDate(String date) {
        try {
            return inputParser.parse(date);
        } catch (java.text.ParseException e) {
            return new Date(0);
        }
    }



    /**
     * This allow us to go back to logIn Screen
     */

    @Override
    public void onBackPressed() {
        user.logOutUser();

        Intent intent = new Intent(HomeScreen.this, LoginScreen.class);
        intent.putExtra(userId, user);
        startActivity(intent);
    }

}