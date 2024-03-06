package com.example.railinfo.viewholders;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.railinfo.R;
import com.example.railinfo.ServiceInfoActivity;
import com.example.railinfo.data.objects.ServiceData;

/**
 * Defines the structure of each view within the Arrivals/Departures RecyclerView and the
 * methods to control what data is displayed on the view.
 * <p>
 * Used by the {@link com.example.railinfo.adapters.ArrivalsAdapter}
 * & {@link com.example.railinfo.adapters.DepartureAdapter}.
 *
 * @author Sam Townley
 * @version 1.2
 */
public class ServiceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    /** The context for the activity that the view holder belongs to. */
    private final Context context;

    /** A text view displaying the name of either the origin or destination of the service. */
    private final TextView title;

    /** A text view for displaying additional information under the title, such as the via. */
    private final TextView subtitle;

    /** A text view displaying the scheduled arrival or departure of the service. */
    private final TextView time;

    /** A text view displayed the current status of the service underneath the time. */
    private final TextView status;

    /** A text view displaying the platform number the service will arrive/depart on. */
    private final TextView platform;

    /** A text view displaying a static dash between the journey_time and operator. */
    private final TextView dash;

    /** A text view displaying the time until the service reaches it's destination. */
    private final TextView journey_time;

    /** A text view displaying the name of the train operating company. */
    private final TextView operator;

    /** The ServiceData object currently being displayed by the view holder. */
    private ServiceData service = null;

    /**
     * Constructs a new view holder for the editing the contents of a service view.
     *
     * @param view The view that was inflated from the layout by the DepartureAdapter or
     *             ArrivalsAdapter.
     * @param context The context for the activity that the view holder belongs to.
     */
    public ServiceHolder(View view, Context context) {
        // Initialise the parent view
        super(view);

        // Set the on click listener to be this class so the onClick() method is used.
        view.setOnClickListener(this);
        this.context = context;

        // Fetch the necessary views from the layout
        title = view.findViewById(R.id.txt_station);
        subtitle = view.findViewById(R.id.txt_subinfo);
        time = view.findViewById(R.id.txt_departure);
        status= view.findViewById(R.id.txt_status);
        platform = view.findViewById(R.id.txt_platform_no);
        dash = view.findViewById(R.id.txt_dash);
        journey_time = view.findViewById(R.id.txt_journey_time);
        operator = view.findViewById(R.id.txt_operator);
    }

    /**
     * Called when this view holder has been clicked.
     * <p>
     * Starts the ServiceInfoActivity with the service stored by this view holder
     * at the time it was clicked.
     *
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        // Check this view holder is correctly associated with a service before doing anything
        if (service != null) {

            // Start the ServiceInfoActivity using an explicit intent
            Intent intent = new Intent(context, ServiceInfoActivity.class);
            intent.putExtra("service", service.toString());
            context.startActivity(intent);

        // Warn the console if the service is null when the view holder was clicked.
        } else {
            System.out.println("Service is null");
        }
    }

    /**
     * Sets the ServiceData object currently being stored by this view holder.
     *
     * @param service The ServiceData object representing the service currently being displayed.
     */
    public void setService(ServiceData service) {
        this.service = service;
    }

    /**
     * Sets the name of either the origin or destination of the service, depending on the
     * adapter that calls this method.
     *
     * @param name The origin or destination of the service.
     */
    public void setStation(String name) {
        title.setText(name);
    }

    /**
     * Sets the subtext of the service view holder.
     * <p>
     * This will either be a via station if one exists, or the destination of the service
     * if called from the ArrivalsAdapter.
     *
     * @param text The text to display inside the subtext.
     */
    public void setSubtext(String text) {
        // Make sure the subtitle is visible before changing the text.
        subtitle.setVisibility(View.VISIBLE);
        subtitle.setText(text);
    }

    /**
     * Hides the subtext view if it isn't being used.
     */
    public void removeSubtext() {
        subtitle.setVisibility(View.GONE);
    }

    /**
     * Sets the displayed arrival/departure time of the service.
     *
     * @param time_str The arrival/departure time of the service, formatted as a string.
     */
    public void setTime(String time_str) {
        time.setText(time_str);
    }

    /**
     * Sets the status of the service to cancelled, with a red colour.
     */
    public void setCancelled() {
        status.setText(context.getString(R.string.cancelled));
        status.setTextColor(context.getColor(R.color.cancelled));
    }

    /**
     * Displays the status of the service as delayed, with the new expected arrival/departure time.
     * <p>
     * Will just display Delayed if the service is indefinitely delayed.
     *
     * @param exp_time The expected arrival/departure time, formatted as a string.
     */
    public void setDelayed(String exp_time) {
        if (exp_time.equals("Delayed")) {
            status.setText(exp_time);
        } else {
            status.setText(context.getString(R.string.exp, exp_time));
        }
        status.setTextColor(context.getColor(R.color.delayed));
    }

    /**
     * Sets the status of the service to On Time, with a green colour.
     */
    public void setOnTime() {
        status.setText(context.getString(R.string.on_time));
        status.setTextColor(context.getColor(R.color.on_time));
    }

    /**
     * Sets the platform number the service is due to arrive/depart on.
     *
     * @param platform_no The platform number the service is expected to use.
     */
    public void setPlatform(String platform_no) {
        platform.setText(platform_no);
    }

    /**
     * Sets the name of the train operator company running the service.
     *
     * @param name The name of the service operator.
     */
    public void setOperator(String name) {
        operator.setText(name);
    }

    /**
     * Sets the time until the service is due to reach it's destination.
     *
     * @param time The time left in the service's journey, as a human readable string.
     */
    public void setJourneyTime(String time) {
        journey_time.setText(time);
        journey_time.setVisibility(View.VISIBLE);
        dash.setVisibility(View.VISIBLE);
    }

    /**
     * Hides the journey time view and subsequent dash between the journey time and operator if
     * this view isn't being used.
     * <p>
     * Always hidden for arriving services using the ArrivalsAdapter.
     */
    public void removeJourneyTime() {
        dash.setVisibility(View.GONE);
        journey_time.setVisibility(View.INVISIBLE);
        journey_time.setText("");
    }
}
