package com.example.railinfo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.railinfo.R;
import com.example.railinfo.viewholders.CallingPointHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * An adapter for displaying a visual map of the a service's journey using a RecyclerView.
 * <p>
 * Displays a tube-map style of a service's journey, showing all stations it stops at along
 * it's route, as well as it's current progress. Also allows you to see it's estimated departure
 * for all future stations, as well as whether the service is running to time.
 *
 * @author Sam Townley
 * @version 1.0
 */
public class CallingPointAdapter extends RecyclerView.Adapter<CallingPointHolder> {

    /** An array of JSONObjects, each representing a calling point. */
    private final JSONArray stations;

    /** The context for the activity that the adapter belongs to. */
    private final Context context;

    /**
     * Creates a new instance of the CallingPointAdapter with the specified dataset.
     *
     * @param context The context of the activity that the adapter belongs to.
     * @param stations A JSONArray of JSONObjects, each representing a calling point along
     *                 the services journey.
     */
    public CallingPointAdapter(Context context, JSONArray stations) {
        this.context = context;
        this.stations = stations;
    }

    /**
     * Creates a new ViewHolder for adding a new view onto the RecyclerView. Automatically
     * invoked by the layout manager.
     *
     * @param viewGroup The ViewGroup into which the new View will be added after it is bound to
     *                  an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return The CallingPointHolder containing the methods for adding data to the View.
     */
    @NonNull
    @Override
    public CallingPointHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Inflate the calling_point.xml layout, used for all calling points
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.calling_point, viewGroup, false);

        return new CallingPointHolder(view, context);
    }

    /**
     * Replaces the contents of a view within the RecyclerView as it's moved up and down.
     * Invoked automatically by the layout manager.
     * <p>
     * Binds all the necessary information for each calling point object to the calling point
     * layout, using the methods defined in the CallingPointHolder.This includes changing the
     * shape and colour of the map to show the progress of the service.
     *
     * @param viewHolder The ServiceHolder which should be updated to represent the contents of the
     *                   item at the given position in the data set.
     * @param position The position of the item within the dataset.
     */
    @Override
    public void onBindViewHolder(@NonNull CallingPointHolder viewHolder, final int position) {
        // Get the JSONObject representing the calling point
        JSONObject station;
        try {
            station = stations.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to load station calls");
        }

        // If the JSONObject has an actual time, then it must of departed, so set it as visited
        boolean visited = false;
        if (!station.isNull("at")) {
            viewHolder.setVisited();
            visited = true;
        } else {
            viewHolder.setToVisit();
        }

        // If the first position, set as origin, or if position is last in array, set as destination.
        // Need to reset viewHolder if neither of these so map stays correct as the user scrolls.
        if (position == 0) {
            viewHolder.setAsOrigin();
        } else if (position == (stations.length()-1)) {
            viewHolder.setAsDestination();
        } else {
            viewHolder.setNormal();
        }

        // Set the station name and scheduled time of departure (or arrival if the destination)
        viewHolder.setStation(station.optString("locationName") + " (" + station.optString("crs") + ")");
        viewHolder.setTime(station.optString("st"));

        // Set the status of the service, based on whether it has departed already or not.
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

    /**
     * Returns the number of calling points a service has along its route.
     * Invoked by the layout manager.
     *
     * @return The size of the dataset.
     */
    @Override
    public int getItemCount() {
        return stations.length();
    }
}
