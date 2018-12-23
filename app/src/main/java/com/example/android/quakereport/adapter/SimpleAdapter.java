package com.example.android.quakereport.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.quakereport.R;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

import entity.Earthquake;

public class SimpleAdapter extends ArrayAdapter<Earthquake> {

    private List<Earthquake> earthquakes;

    public SimpleAdapter(Context context, int resource, List<Earthquake> earthquakes) {
        super(context, resource, earthquakes);
        this.earthquakes = earthquakes;
    }


    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_list, parent, false);
        }

        Earthquake earthquake = getItem(position);


        TextView mag = listItemView.findViewById(R.id.magnitude);
        TextView locationOffset = listItemView.findViewById(R.id.locationOffset);
        TextView place = listItemView.findViewById(R.id.place);
        TextView date = listItemView.findViewById(R.id.date);
        TextView time = listItemView.findViewById(R.id.time);

        GradientDrawable magnitudeCircle = (GradientDrawable) mag.getBackground();
        magnitudeCircle.setColor(getMagnitudeColor(earthquake.getMagnitude()));

        mag.setText(String.valueOf(earthquake.getMagnitude()));
        String [] places =  getPlaces(earthquake.getPlace());
        locationOffset.setText(places[0]);
        place.setText(places[1]);
        date.setText(earthquake.getDate().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
        time.setText(earthquake.getDate().format(DateTimeFormatter.ofPattern("hh:mm a")));

        return listItemView;
    }


    private String[] getPlaces(String s) {
        String[] result = new String[2];
        if(s.contains("of")) {
            result = s.split("of");
            result[0] = result[0] + "of ";
        }
        else {
            result[0] = "Near the";
            result[1] = s;
        }

        return result;
    }


    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;

            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;

            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;

            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;

            case 10:
                magnitudeColorResourceId = R.color.magnitude4;
                break;

            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;

        }

        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }

    public void setEarthquakes(List<Earthquake> earthquakes) {
        clear();
        super.addAll(earthquakes);
        this.earthquakes = earthquakes;
    }

    public List<Earthquake> getEarthquakes() {
        return earthquakes;
    }

    @Override
    public void clear() {
        this.earthquakes.clear();
        super.clear();

    }
}
