package com.lasalle.meet;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.lasalle.meet.enities.Event;
import com.lasalle.meet.enities.User;
import com.lasalle.meet.exceptions.eventexceptions.EventInvalidCredentialsException;

import org.jetbrains.annotations.NotNull;

public class EventView extends AppCompatActivity {

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

        eventName.setText(event.getName());

        eventAddress.setText(event.getLocation());

        eventType.setText(event.getType());

        eventDate.setText(event.getDate());

        eventImage.setImageDrawable(getDrawable(R.drawable.defect_image_event));

        mButton = (MaterialButton) findViewById(R.id.signup_button);

        mFloatingButton = (FloatingActionButton) findViewById(R.id.editProfileInfoButton);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (event.getOwner_id() != user.getId()) {
                    try {
                        event.eventAssist(user.getAccessToken());
                    } catch (EventInvalidCredentialsException e) {
                        Toast.makeText(EventView.this, "There has been an error joining", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    event.deleteEvent(user.getAccessToken());
                    onBackPressed();
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

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),15));
                }else {
                    Toast.makeText(EventView.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EventView.this, EventTimeline.class);
        intent.putExtra(userId, user);
        startActivity(intent);
    }
}
