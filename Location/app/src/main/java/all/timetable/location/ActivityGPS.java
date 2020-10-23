package all.timetable.location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.Date;

import all.timetable.location.model.CoordinateGPS;

public class ActivityGPS extends AppCompatActivity {
    private CoordinateGPS coordinate;
    Button button;
    TextView textView;
    TextView textView2;
    private static final int REQUEST_CODE_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        button = findViewById(R.id.requesBTN);
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.coordinates);

//        String geoURI = String.format("geo:%f,%f?z=10", 53.98, 27.65); // использовать другой формат запроса?
//        Uri geo = Uri.parse(geoURI);
//        Intent geoMap = new Intent(Intent.ACTION_VIEW, geo);
//        startActivity(geoMap);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ActivityGPS.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION);

        } else {
            getCurrentLocation();
        }

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION && requestCode > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "PERMISSION DENIED", Toast.LENGTH_SHORT);
            }
        }
    }


    private void getCurrentLocation() {
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
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
        LocationServices.getFusedLocationProviderClient(ActivityGPS.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(ActivityGPS.this).removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int index = locationResult.getLocations().size() - 1;
                            double latitude = locationResult.getLocations().get(index).getLatitude();
                            double longitude = locationResult.getLocations().get(index).getLongitude();
                           // coordinate=new Coordinate(latitude,longitude);
                            textView.setText(String.format("Latitude: %s\n Longitude: %s", latitude, longitude));
                            textView2.setText((String.format(
                                    "Coordinates: lat = %1$.4f, lon = %2$.4f, time = %3$tF %3$tT",
                                    locationResult.getLocations().get(index).getLatitude(), locationResult.getLocations().get(index).getLongitude(), new Date(
                                            locationResult.getLocations().get(index).getTime()))));

                        }
                    }
                }, Looper.getMainLooper());
    }
}
