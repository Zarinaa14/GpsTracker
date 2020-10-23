package all.timetable.location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.visuality.f32.temperature.TemperatureUnit;
import com.visuality.f32.weather.data.entity.Weather;
import com.visuality.f32.weather.manager.WeatherManager;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import all.timetable.location.model.CoordinateGPS;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    private Button permissionBTN;
    private TextView addressGPSTextView;
    private TextView coordinatesTextView;
    private TextView weatherTextView;
    private static final int REQUEST_CODE_PERMISSION = 1;
    List<CoordinateGPS> list;
    List<CoordinateGPS> coordinateGPSList;


    private LatLng marker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        list = new ArrayList<>();
        permissionBTN = findViewById(R.id.requesBTN);
        addressGPSTextView = findViewById(R.id.address);
        coordinatesTextView = findViewById(R.id.coordinates);
        weatherTextView = findViewById(R.id.weather);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.dashboard:
                        Intent goToActivity = new Intent(MapsActivity.this, StorageRequestActivity.class);
                        goToActivity.putExtra("ITEMS", (Serializable) list);
                        startActivity(goToActivity);

                        //then on second activity

                        // startActivity(new Intent(getApplicationContext(), MainActivity2.class)); без передачи листа
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.home:
                        return true;
                }
                return false;
            }
        });



// Get data from activity
        Intent i = getIntent();
        coordinateGPSList = (ArrayList<CoordinateGPS>) i.getSerializableExtra("ITEMS");
        if (coordinateGPSList != null) {
            list = coordinateGPSList;
        }
//if we have permission getCurrentLocation
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION);

        } else {
            getCurrentLocation();
        }
//new request
        permissionBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION);

                } else {
                    Toast.makeText(MapsActivity.this, "new request", Toast.LENGTH_SHORT).show();
                    getCurrentLocation();

                }

            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        View view = super.onCreateView(name, context, attrs);


        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION && requestCode > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();


            } else {
                Toast.makeText(this, "PERMISSION DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

//MissingPermission because we already check permission for LocationServices
    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(MapsActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(MapsActivity.this).removeLocationUpdates(this);
                        //   LocationServices.getFusedLocationProviderClient(MapsActivity.this).requestLocationUpdates()

                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int index = locationResult.getLocations().size() - 1;
                            double latitude = locationResult.getLocations().get(index).getLatitude();
                            double longitude = locationResult.getLocations().get(index).getLongitude();
                            marker = new LatLng(latitude, longitude);
//Weather API
                            new WeatherManager("65694c1e412e714bcf5829a9a1b993fc").getCurrentWeatherByCoordinates(
                                    latitude, // latitude
                                    longitude, // longitude
                                    new WeatherManager.CurrentWeatherHandler() {
                                        @Override
                                        public void onReceivedCurrentWeather(WeatherManager manager, Weather weather) {
                                            weather.getTemperature();
                                            weather.getWind();
                                            weather.getTemperature().getCurrent();

                                            double currentTemperatureInCelcius = weather.getTemperature().getCurrent()
                                                    .getValue(TemperatureUnit.CELCIUS);

                                            double windSpeed = weather.getWind().getSpeed();

                                            weatherTextView.setText(currentTemperatureInCelcius+"℃\n"
                                                  +"wind:"+windSpeed+"м/с");
                                        }

                                        @Override
                                        public void onFailedToReceiveCurrentWeather(WeatherManager manager) {
                                           int y=0;
                                           y++;
                                        }
                                    }
                            );

                            locationResult.getLastLocation();
                            long time = locationResult.getLocations().get(index).getTime();
                            Date date = getDateFormat(time);



                            //  MapsActivity.this.addressGPS.setText(String.format("Latitude: %s\n Longitude: %s", latitude, longitude));
                            coordinatesTextView.setText((String.format(
                                    "Coordinates: \nlat = %1$.4f,\nlon = %2$.4f, \ndata and time = %3$tF %3$tT",
                                    locationResult.getLocations().get(index).getLatitude(), locationResult.getLocations().get(index).getLongitude(), new Date(
                                            locationResult.getLocations().get(index).getTime()))));

                            locationResult.getLocations();
                            Geocoder geocoder;
                            List<Address> addresses = null;
                            geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
//get address from geocoder
                            try {
                                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                String address = addresses.get(0).getAddressLine(0);
                                String city = addresses.get(0).getLocality();
                                String state = addresses.get(0).getAdminArea();
                                String country = addresses.get(0).getCountryName();
                                String postalCode = addresses.get(0).getPostalCode();
                                list.add(new CoordinateGPS(latitude, longitude, String.valueOf(date),address,city,state,country,postalCode));

                              addressGPSTextView.setText(address+"\ndistrict: "+state);
                            } catch (IOException e) {
                                addressGPSTextView.setText(e.getMessage());
                            }

                            if (mMap != null) {
                                mMap.addMarker(new MarkerOptions().position(marker).title("You are here!"));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
                            }

                        }
                    }
                }, Looper.getMainLooper());


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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (marker != null) {
            mMap.addMarker(new MarkerOptions().position(marker).title("home"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
        }
    }


    private Date getDateFormat(long time) {
        Date result = new Date(time);

        return result;
    }


}
