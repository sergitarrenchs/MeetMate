package com.lasalle.meet;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.lasalle.meet.enities.Event;
import com.lasalle.meet.enities.User;
import com.lasalle.meet.exceptions.eventexceptions.EventException;
import com.lasalle.meet.exceptions.eventexceptions.EventInvalidCredentialsException;
import com.lasalle.meet.exceptions.eventexceptions.EventNullImageException;
import com.lasalle.meet.exceptions.eventexceptions.EventNullLocationException;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class FinishEvent extends AppCompatActivity {
    private GoogleMap mMap;
    private ImageButton CreateButton;
    private MaterialButton uploadEventPicture;

    private User user;
    private static String userId = "USER_ID";

    private static String event_name = "EVENT_NAME";
    private static String event_description = "EVENT_DESCRIPTION";
    private static String event_startDate = "EVENT_START_DATE";
    private static String event_endDate = "EVENT_END_DATE";
    private static String event_Type = "EVENT_TYPE";
    private static String event_Num = "EVENT_NUM";
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private static final int REQUEST_CODE_SELECT_IMAGE = 2;
    private static final int REQUEST_CODE_CALENDAR = 3;

    private String eventName;
    private String eventDescription;
    private Date eventStartDate;
    private Date eventEndDate;
    private String eventType;
    private int eventMaxParticipants;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private TextInputEditText eventAddress;
    private TextInputLayout eventAddress_layout;
    private ImageView imageSelected;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_event_2_activity);

        user = (User) getIntent().getSerializableExtra(userId);

        eventName = (String) getIntent().getSerializableExtra(event_name);
        eventDescription = (String) getIntent().getSerializableExtra(event_description);

        try {
            eventStartDate = new SimpleDateFormat("dd / MM / yyyy HH : mm").parse((String) getIntent().getSerializableExtra(event_startDate));
        } catch (ParseException e) {
            eventStartDate = null;
        }
        try {
            eventEndDate = new SimpleDateFormat("dd / MM / yyyy HH : mm").parse((String) getIntent().getSerializableExtra(event_endDate));
        } catch (ParseException e) {
            eventEndDate = null;
        }

        eventType = (String) getIntent().getSerializableExtra(event_Type);

        eventMaxParticipants = (int) getIntent().getSerializableExtra(event_Num);

        eventAddress = (TextInputEditText) findViewById(R.id.event_location);

        eventAddress_layout = (TextInputLayout) findViewById(R.id.event_location_layout);

        CreateButton = (ImageButton) findViewById(R.id.create_button_image);

        CreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean eventCreated = createNewEvent();

                if (eventCreated) {
                    Intent intent = new Intent(FinishEvent.this, HomeScreen.class);
                    intent.putExtra(userId, user);
                    startActivity(intent);
                }
            }
        });

        uploadEventPicture = (MaterialButton) findViewById(R.id.upload_profile_pic_button2);

        uploadEventPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            FinishEvent.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_STORAGE_PERMISSION
                    );
                } else {
                    selectImage();
                }
            }
        });

        Places.initialize(this, "AIzaSyCuUpbi0fy0VT9rvz2XD85pYnj69ZtpfjA");

        eventAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> places = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);

                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, places).build(FinishEvent.this);

                startActivityForResult(intent, REQUEST_CODE_CALENDAR);
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.event_location_mapView);
        mapFragment.getMapAsync(this::onMapReady);

        getLocation();
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CALENDAR && resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            eventAddress.setText(place.getAddress());

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(Objects.requireNonNull(place.getLatLng()));

            mMap.clear();

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15));

            mMap.addMarker(markerOptions);
        } else if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedImageUri = data.getData();

                if (selectedImageUri != null) {
                    try {

                        InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                        imageSelected.setImageBitmap(bitmap);

                    } catch (Exception exception) {
                        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
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
                    Toast.makeText(FinishEvent.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull @NotNull LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);

                mMap.clear();

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));

                mMap.addMarker(markerOptions);

                Geocoder geocoder = new Geocoder(FinishEvent.this, Locale.getDefault());

                try {
                    List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

                    String address = addresses.get(0).getAddressLine(0);
                    eventAddress.setText(address);

                } catch (IOException e) {
                    String nullAddress = latLng.latitude + " , " + latLng.longitude;
                    eventAddress.setText(nullAddress);
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
        Intent intent = new Intent(FinishEvent.this, NewEvent.class);
        intent.putExtra(userId, user);
        startActivity(intent);
    }

    private boolean createNewEvent() {
        try {
            Event.createNewEvent(eventName, eventAddress.getText().toString(), eventDescription, eventStartDate, eventEndDate, eventMaxParticipants, eventType, "imageString", user.getAccessToken());

            return true;
        } catch (EventNullLocationException e) {
            eventAddress_layout.setError("The address is empty");
        } catch (EventNullImageException e) {
            eventAddress_layout.setError("The image is empty");
        } catch (EventInvalidCredentialsException e) {
            eventAddress_layout.setError("Invalid parameters");
        } catch (EventException ignored) {}

        return false;

    }
}
