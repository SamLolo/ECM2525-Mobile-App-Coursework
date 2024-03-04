package com.example.railinfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DepartureAdapter extends RecyclerView.Adapter<ServiceHolder> {
    private final ArrayList<ServiceData> services;
    private final Context context;

    public DepartureAdapter(Context context, ArrayList<ServiceData> services) {
        this.context = context;
        this.services = services;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ServiceHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.service, viewGroup, false);

        return new ServiceHolder(view, context);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ServiceHolder viewHolder, final int position) {
        ServiceData service = services.get(position);
        viewHolder.setService(service);

        viewHolder.setStation(service.getDestinationString());
        if (service.hasVia()) {
            viewHolder.setSubtext(service.getVia());
        } else {
            viewHolder.removeSubtext();
        }

        viewHolder.setTime(service.getScheduledDeparture());
        viewHolder.setPlatform(service.getPlatformString());
        viewHolder.setOperator(service.getOperator());

        String journeyTime = service.getTimeToDestination();
        if (!journeyTime.equals("N/A")) {
            viewHolder.setJourneyTime(journeyTime);
        } else {
            viewHolder.removeJourneyTime();
        }

        if (service.isCancelled()) {
            viewHolder.setCancelled();
        } else if (service.isDelayedDeparture()) {
            viewHolder.setDelayed(service.getEstimatedDeparture());
        } else {
            viewHolder.setOnTime();
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return services.size();
    }
}
