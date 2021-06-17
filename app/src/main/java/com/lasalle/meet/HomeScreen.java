package com.lasalle.meet;
//MAIN COMENT
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.lasalle.meet.enities.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeScreen extends AppCompatActivity {
    private GoogleMap mMap;
    private float x1,x2,y1,y2;

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
                Intent i = new Intent(HomeScreen.this, ProfileScreen.class);
                i.putExtra(userId, user);
                startActivity(i);
            }else if(x1 > x2){
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

        // Add a marker in Sydney and move the camera
        LatLng barcelona = new LatLng(41.390205,2.154007);
        mMap.addMarker(new MarkerOptions().position(barcelona).title("Marker in Barcelona"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(barcelona));
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 15.0f ) );
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