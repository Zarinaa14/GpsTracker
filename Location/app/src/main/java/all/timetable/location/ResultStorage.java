package all.timetable.location;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import all.timetable.location.model.CoordinateGPS;

public class ResultStorage extends AppCompatActivity {
    private TextView text1TextView;
    private TextView text2TextView;
    private TextView text3TextView;
    private String text1;
    private String text2;
    private String text3;

    private List<CoordinateGPS> coordinateGPSList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        text1TextView =findViewById(R.id.text1);
        text2TextView =findViewById(R.id.text2);
        text3TextView =findViewById(R.id.text3);
        Intent i = getIntent();
        text1 = i.getStringExtra(Constants.TEXT1);
        text2 = i.getStringExtra(Constants.TEXT2);
        text3 = i.getStringExtra(Constants.TEXT3);
        text1TextView.setText(text1);
        text2TextView.setText(text2);
        text3TextView.setText(text3);
    }
}