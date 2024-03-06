package com.example.railinfo.viewholders;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.railinfo.R;

/**
 * Defines the structure of each view within the CallingPoint RecyclerView and the methods to
 * control what data is displayed on the view.
 * <p>
 * Used by the {@link com.example.railinfo.adapters.CallingPointAdapter}.
 *
 * @author Sam Townley
 * @version 1.0
 */
public class CallingPointHolder extends RecyclerView.ViewHolder{

    /** The context for the activity that the view holder belongs to. */
    private final Context context;

    /** A text view displaying the departure (or arrival) time */
    private final TextView time;

    /** A text view displaying the status of the service at each calling point */
    private final TextView status;

    /** A text view displaying the name of the station */
    private final TextView station;

    /** A view displaying the top line between stations */
    private final View line_1;

    /** A view displaying the bottom line between stations */
    private final View line_2;

    /** A view displaying a circle, representing the station on the map */
    private final View circle;

    /** Whether the station currently displayed by the view holder has been visited */
    private Boolean visited = false;

    /**
     * Constructs a new view holder for the editing the contents of a calling point view.
     *
     * @param view The view that was inflated from the layout by the CallingPointAdapter.
     * @param context The context for the activity that the view holder belongs to.
     */
    public CallingPointHolder(View view, Context context) {
        // Initialise the parent view
        super(view);
        this.context = context;

        // Fetch the necessary views from the layout
        time = view.findViewById(R.id.calling_point_time);
        status = view.findViewById(R.id.calling_point_status);
        station = view.findViewById(R.id.calling_point_name);
        line_1 = view.findViewById(R.id.line_1);
        line_2 = view.findViewById(R.id.line_2);
        circle = view.findViewById(R.id.circle);
    }

    /**
     * Sets the calling point as being visited by changing the color of the lines,
     * and filling in the circle.
     */
    public void setVisited() {
        // Set the color of the 2 lines to the "visited" colour scheme
        line_1.setBackgroundColor(context.getColor(R.color.visited));
        line_2.setBackgroundColor(context.getColor(R.color.visited));

        // Swap the circle to the filled in circle, which has the visited colour scheme
        Resources res = context.getResources();
        circle.setBackground(ResourcesCompat.getDrawable(res, R.drawable.filled_circle, null));

        // Set the boolean flag to true so the status is set correctly
        visited = true;
    }

    /**
     * Sets the calling point in a "to visit" state.
     * This is the default state for a new calling point view.
     */
    public void setToVisit() {
        // Set the color of the 2 lines back to the default "to visit"
        line_1.setBackgroundColor(context.getColor(R.color.to_visit));
        line_2.setBackgroundColor(context.getColor(R.color.to_visit));

        // Swap the circle shape back to unvisited outline
        Resources res = context.getResources();
        circle.setBackground(ResourcesCompat.getDrawable(res, R.drawable.circle_outline, null));

        // Reset the boolean flag so that the status is set correctly
        visited = false;
    }

    /**
     * Sets the text within the time view.
     *
     * @param departure The string representing the time of arrival/departure from the
     *                  calling point.
     */
    public void setTime(String departure) {
        time.setText(departure);
    }

    /**
     * Sets the text within the station view.
     *
     * @param name The name of the station to display.
     */
    public void setStation(String name) {
        station.setText(name);
    }

    /**
     * Set the calling point as the origin location by hiding the line above the circle.
     */
    public void setAsOrigin() {
        line_1.setVisibility(View.INVISIBLE);
        line_2.setVisibility(View.VISIBLE);
    }

    /**
     * Set the calling point as the destination by hiding the line below the circle.
     */
    public void setAsDestination() {
        line_1.setVisibility(View.VISIBLE);
        line_2.setVisibility(View.INVISIBLE);
    }

    /**
     * Resets the calling point to it's default state (lines above and below the circle).
     * Used to reset views as the RecyclerView scrolls up and down.
     */
    public void setNormal() {
        line_1.setVisibility(View.VISIBLE);
        line_2.setVisibility(View.VISIBLE);
    }

    /**
     * Sets the status of the service to cancelled, displayed in red text.
     */
    public void setCancelled() {
        status.setText(context.getString(R.string.cancelled));
        status.setTextColor(context.getColor(R.color.cancelled));
    }

    /**
     * Sets the status of the service as delayed, with an orange colour.
     * <p>
     * Will display "Departed xx:xx" if the station has already been visited instead of
     * "Expected xx:xx".
     *
     * @param time The expected time of departure (or actual time of departure if the service
     *             has already departed).
     */
    public void setDelayed(String time) {
        status.setTextColor(context.getColor(R.color.delayed));
        if (!visited) {
            status.setText(context.getString(R.string.expected, time));
        } else {
            status.setText(context.getString(R.string.delayed_departure, time));
        }
    }

    /**
     * Sets the status of the service as on time, with a green colour.
     * <p>
     * Will display "Departed On Time" if the station has already been visited instead of
     * "On Time".
     */
    public void setOnTime() {
        status.setTextColor(context.getColor(R.color.on_time));
        if (!visited) {
            status.setText(context.getString(R.string.on_time));
        } else {
            status.setText(context.getString(R.string.on_time_departure));
        }
    }
}
