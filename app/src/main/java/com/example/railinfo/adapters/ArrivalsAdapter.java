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

import org.json.JSONException;

import java.util.ArrayList;

public class ArrivalsAdapter extends RecyclerView.Adapter<ServiceHolder> {
    private final ArrayList<ServiceData> services;
    private final Context context;

    public ArrivalsAdapter(Context context, ArrayList<ServiceData> services) {
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

        viewHolder.setStation(service.getOriginString());
        try {
            if (service.terminatesHere()) {
                viewHolder.setSubtext(context.getString(R.string.terminates_here));
            } else {
                viewHolder.setSubtext(context.getString(R.string.to_with_destination, service.getDestinationString()));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        viewHolder.setTime(service.getScheduledArrival());
        viewHolder.setPlatform(service.getPlatformString());
        viewHolder.setOperator(service.getOperator());
        viewHolder.removeJourneyTime();

        if (service.isCancelled()) {
            viewHolder.setCancelled();
        } else if (service.isDelayedArrival()) {
            viewHolder.setDelayed(service.getEstimatedArrival());
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
