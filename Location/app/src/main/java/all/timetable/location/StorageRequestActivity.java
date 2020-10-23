package all.timetable.location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import all.timetable.location.adapter.ListAdapter;
import all.timetable.location.model.CoordinateGPS;

public class StorageRequestActivity extends AppCompatActivity {
    private ListView list;
    List<CoordinateGPS> coordinateGPSList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storage_request);

        coordinateGPSList = new ArrayList<>();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.dashboard);
        list = findViewById(R.id.list);


        Intent i = getIntent();
        coordinateGPSList = (ArrayList<CoordinateGPS>) i.getSerializableExtra("ITEMS");


        List<String> strings = getLatitudeList(coordinateGPSList);
        List<String> strings2 = getLongitudeList(coordinateGPSList);
        List<String> strings3 = getDateList(coordinateGPSList);


        ListAdapter adapter = new ListAdapter(this, strings, strings2, strings3);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String topicNameByPosition = ((ListAdapter) list.getAdapter()).getTopicNameByPosition(position);
//Ниже можно вывести данные через диалог
//                AlertDialog alertDialog = new AlertDialog.Builder(StorageRequestActivity.this).create();
//                alertDialog.setTitle("Город: "+coordinateGPSList.get(position).getCity());
//                alertDialog.setMessage("Country: "+coordinateGPSList.get(position).getCountry()+
//                        "\nCity: "+coordinateGPSList.get(position).getCity()+
//                        "\nAddress: "+coordinateGPSList.get(position).getAddress()+"\nData: "+coordinateGPSList.get(position).getDate()
//                );
//                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(getApplicationContext(), "return to activity", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                alertDialog.show();


                Intent i = new Intent(StorageRequestActivity.this, ResultStorage.class);
                i.putExtra(Constants.TEXT1, "District: " + String.valueOf(coordinateGPSList.get(position).getState()));
                i.putExtra(Constants.TEXT2, "Country: " + String.valueOf(coordinateGPSList.get(position).getCountry() +
                        "\nCity: " + coordinateGPSList.get(position).getCity() + "\nAddress: " + coordinateGPSList.get(position).getAddress()));
                i.putExtra(Constants.TEXT3, "\nData: " + coordinateGPSList.get(position).getDate());
                startActivity(i);

            }
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.dashboard:
                        return true;
                    case R.id.home:

                        Intent goToActivity = new Intent(StorageRequestActivity.this, MapsActivity.class);
                        goToActivity.putExtra("ITEMS", (Serializable) coordinateGPSList);
                        startActivity(goToActivity);
                        //  startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

    }

    private List<String> getLatitudeList(List<CoordinateGPS> coordinateGPS) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < coordinateGPS.size(); i++) {
            list.add(String.valueOf(coordinateGPS.get(i).getLatitude()));
        }
        return list;
    }

    private List<String> getLongitudeList(List<CoordinateGPS> coordinateGPS) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < coordinateGPS.size(); i++) {
            list.add(String.valueOf(coordinateGPS.get(i).getLongitude()));
        }
        return list;
    }

    private List<String> getDateList(List<CoordinateGPS> coordinateGPS) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < coordinateGPS.size(); i++) {
            list.add(String.valueOf(coordinateGPS.get(i).getDate()));
        }
        return list;
    }

}