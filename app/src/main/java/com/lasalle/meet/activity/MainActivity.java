package com.lasalle.meet.activity;
//MAIN COMENT
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lasalle.meet.R;
import com.lasalle.meet.entity.APIAdapter;
import com.lasalle.meet.entity.JsonPlaceHolderAPI;
import com.lasalle.meet.entity.User;
import com.lasalle.meet.exception.UserException;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private GoogleMap mMap;

    private JsonPlaceHolderAPI jsonPlaceHolderAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this::onMapReady);

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://puigmal.salle.url.edu/api/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        jsonPlaceHolderAPI = retrofit.create(JsonPlaceHolderAPI.class);


        createUser();

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

    private void createUser() {
        User user = new User();
        try {
            user = user.signUpUser("test","local@host","1234567890", "1234567890");
        } catch (UserException e) {
            System.out.println("ERROR CREATE USER");
        }

        Call<User> call = APIAdapter.getApiService().postCreateUser(user);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful()){
                    System.out.println("Code: " + response.code());
                    return;
                }

                User userResponse = response.body();

                System.err.println(userResponse.toString());

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
}