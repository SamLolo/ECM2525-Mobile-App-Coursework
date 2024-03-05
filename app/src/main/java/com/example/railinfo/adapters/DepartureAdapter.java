package com.example.railinfo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.railinfo.R;
import com.example.railinfo.data.objects.ServiceData;
import com.example.railinfo.viewholders.ServiceHolder;

import java.util.ArrayList;

/**
 * An Adapter for displaying information about departing services within a RecyclerView.
 * Inflates and uses the service.xml layout, displaying the destination and departure
 * time of the service.
 * <p>
 * Requires that all services passed to the adapter are departures. If this is not the case,
 * the service will be displayed incorrectly, and may cause visual issues. No verification
 * is carried out within this class.
 *
 * @author Sam Townley
 * @version 1.0
 */
public class DepartureAdapter extends RecyclerView.Adapter<ServiceHolder> {

    /** The array of departing services specified when calling the constructor. */
    private final ArrayList<ServiceData> services;

    /** The context for the activity that the adapter belongs to. */
    private final Context context;

    /**
     * Creates a new instance of the DepartureAdapter with the specified dataset.
     *
     * @param context The context of the activity that the adapter belongs to.
     * @param services An ArrayList of ServiceData objects representing departing services.
     */
    public DepartureAdapter(Context context, ArrayList<ServiceData> services) {
        this.context = context;
        this.services = services;
    }

    /**
     * Creates a new ViewHolder for adding a new view onto the RecyclerView. Automatically
     * invoked by the layout manager.
     *
     * @param viewGroup The ViewGroup into which the new View will be added after it is bound to
     *                  an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return The ServiceHolder containing the methods for adding data to the View.
     */
    @NonNull
    @Override
    public ServiceHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Inflate the service.xml layout, used for all services
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.service, viewGroup, false);

        return new ServiceHolder(view, context);
    }

    /**
     * Replaces the contents of a view within the RecyclerView as it's moved up and down.
     * Invoked automatically by the layout manager.
     * <p>
     * Binds all the necessary information stored inside {@link com.example.railinfo.data.objects.ServiceData} object to the TextView's
     * within the service.xml layout, using the methods defined in the ServiceHolder.
     *
     * @param viewHolder The ServiceHolder which should be updated to represent the contents of the
     *                   item at the given position in the data set.
     * @param position The position of the item within the dataset.
     */
    @Override
    public void onBindViewHolder(@NonNull ServiceHolder viewHolder, final int position) {
        // Get the ServiceData object of the service being bound to a view.
        ServiceData service = services.get(position);
        viewHolder.setService(service);

        // Set the destination of the service, and add a subtitle if the service goes via a particular station
        viewHolder.setStation(service.getDestinationString());
        if (service.hasVia()) {
            viewHolder.setSubtext(service.getVia());
        } else {
            viewHolder.removeSubtext();
        }

        // Set other general attributes of the service
        viewHolder.setTime(service.getScheduledDeparture());
        viewHolder.setPlatform(service.getPlatformString());
        viewHolder.setOperator(service.getOperator());

        // Display a journey time if one is available (should exist for all departures)
        String journeyTime = service.getTimeToDestination();
        if (!journeyTime.equals("N/A")) {
            viewHolder.setJourneyTime(journeyTime);
        } else {
            viewHolder.removeJourneyTime();
        }

        // Set the status of the service
        if (service.isCancelled()) {
            viewHolder.setCancelled();
        } else if (service.isDelayedDeparture()) {
            viewHolder.setDelayed(service.getEstimatedDeparture());
        } else {
            viewHolder.setOnTime();
        }
    }

    /**
     * Returns the number of services that can be displayed.
     * Invoked by the layout manager.
     *
     * @return The size of the dataset.
     */
    @Override
    public int getItemCount() {
        return services.size();
    }
}
