package all.timetable.location.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import all.timetable.location.R;

public class ListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final List<String> latitude;
    private final List<String> longitude;
    private final List<String> date;


    public ListAdapter(Activity context, List<String> latitude, List<String> longitude, List<String> date) {
        super(context, R.layout.topics_list, latitude);
        this.context = context;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;

    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {


        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.topics_list, null, true);

        TextView requestTextView = (TextView) rowView.findViewById(R.id.request);
        TextView latitudeTextView = (TextView) rowView.findViewById(R.id.word1);
        TextView longitudeTextView = (TextView) rowView.findViewById(R.id.word2);
        TextView dateTextView = (TextView) rowView.findViewById(R.id.word3);

        requestTextView.setText(position+1 +" запрос: ");
        requestTextView.setTextColor(Color.rgb(90, 138, 156));
        requestTextView.setTextSize(13);
        latitudeTextView.setText("lat: " + latitude.get(position));
        longitudeTextView.setText("long: " + longitude.get(position));
        dateTextView.setText("Дата: " + date.get(position));
        return rowView;
    }

    public String getTopicNameByPosition(int position) {
        return latitude.get(position);
    }
}
