package com.example.railinfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DepartureAdapter extends RecyclerView.Adapter<DepartureHolder> {
    private final ArrayList<ServiceData> services;
    private final Context context;

    public DepartureAdapter(Context context, ArrayList<ServiceData> services) {
        this.context = context;
        this.services = services;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DepartureHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.departures, viewGroup, false);

        return new DepartureHolder(view, context);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(DepartureHolder viewHolder, final int position) {
        ServiceData service = services.get(position);
        if (service.hasVia()) {
            viewHolder.setDestinationWithVia(service.getDestinationString(), service.getVia());
        } else {
            viewHolder.setDestination(service.getDestinationString());
        }

        viewHolder.setTime(service.getScheduledDeparture());
        viewHolder.setPlatform(service.getPlatformString());
        viewHolder.setOperator(service.getOperator());

        String journeyTime = service.getTimeToDestination();
        if (!journeyTime.equals("N/A")) {
            viewHolder.setJourneyTime(journeyTime);
        } else {
            viewHolder.removeInfo1();
        }

        if (service.isCancelled()) {
            viewHolder.setCancelled();
        } else if (service.isDelayedDeparture()) {
            viewHolder.setDelayed(service.getEstimatedDeparture());
        }

        if (service.hasFormationLength()) {
            viewHolder.setFormationLength(service.getFormationLength());
        } else {
            viewHolder.removeInfo2();
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return services.size();
    }
}
