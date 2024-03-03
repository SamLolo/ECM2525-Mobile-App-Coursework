package com.example.railinfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CallingPointAdapter extends RecyclerView.Adapter<CallingPointHolder> {
    private final JSONArray stations;
    private final Context context;

    public CallingPointAdapter(Context context, JSONArray stations) {
        this.context = context;
        this.stations = stations;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public CallingPointHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.calling_point, viewGroup, false);

        return new CallingPointHolder(view, context);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull CallingPointHolder viewHolder, final int position) {
        JSONObject station;
        try {
            station = stations.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to load station calls");
        }

        boolean visited = false;
        if (!station.isNull("at")) {
            viewHolder.setVisited();
            visited = true;
        } else {
            viewHolder.setToVisit();
        }

        if (position == 0) {
            viewHolder.setAsOrigin();
        } else if (position == (stations.length()-1)) {
            viewHolder.setAsDestination();
        } else {
            viewHolder.setNormal();
        }

        viewHolder.setStation(station.optString("locationName"));
        viewHolder.setPlatform(0);
        viewHolder.setTime(station.optString("st"));

        if (station.optBoolean("isCancelled")) {
            viewHolder.setCancelled();
        } else if (visited) {
            if (station.optString("at").equals("On time")) {
                viewHolder.setOnTime();
            } else {
                viewHolder.setDelayed(station.optString("at"));
            }
        } else {
            if (station.optString("et").equals("On time")) {
                viewHolder.setOnTime();
            } else {
                viewHolder.setDelayed(station.optString("et"));
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return stations.length();
    }
}
