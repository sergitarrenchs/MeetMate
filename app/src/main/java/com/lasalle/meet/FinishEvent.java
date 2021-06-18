package com.lasalle.meet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.lasalle.meet.enities.Event;
import com.lasalle.meet.enities.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class FinishEvent extends AppCompatActivity {
    private GoogleMap mMap;
    private ImageButton CreateButton;

    private User user;
    private static String userId = "USER_ID";

    private static String event_name = "EVENT_NAME";
    private static String event_description = "EVENT_DESCRIPTION";
    private static String event_startDate = "EVENT_START_DATE";
    private static String event_endDate = "EVENT_END_DATE";
    private static String event_Type = "EVENT_TYPE";
    private static String event_Num = "EVENT_NUM";

    private String eventName;
    private String eventDescription;
    private Date eventStartDate;
    private Date eventEndDate;
    private String eventType;
    private int eventMaxParticipants;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_event_2_activity);

        user = (User) getIntent().getSerializableExtra(userId);

        eventName = (String) getIntent().getSerializableExtra(event_name);
        eventDescription = (String) getIntent().getSerializableExtra(event_description);
        try {
            eventStartDate = new SimpleDateFormat("dd/MM/yyyy").parse((String) getIntent().getSerializableExtra(event_startDate));
        } catch (ParseException e) {
            eventStartDate = null;
        }
        try {
            eventEndDate = new SimpleDateFormat("dd/MM/yyyy").parse((String) getIntent().getSerializableExtra(event_endDate));
        } catch (ParseException e) {
            eventEndDate = null;
        }

        eventType = (String) getIntent().getSerializableExtra(event_Type);

        eventMaxParticipants = (int) getIntent().getSerializableExtra(event_Num);

        CreateButton = (ImageButton) findViewById(R.id.create_button_image);

        CreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinishEvent.this, HomeScreen.class);
                startActivity(intent);
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.event_location_mapView);
        mapFragment.getMapAsync(this::onMapReady);
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

    @Override
    public void onBackPressed() {
        user.logOutUser();

        Intent intent = new Intent(FinishEvent.this, NewEvent.class);
        intent.putExtra(userId, user);
        startActivity(intent);
    }

    private void createNewEvent(){
        //Event.createNewEvent();
    }
}
