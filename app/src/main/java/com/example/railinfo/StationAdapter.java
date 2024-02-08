package com.example.railinfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StationAdapter extends RecyclerView.Adapter<ServiceHolder> {
    private ArrayList<ServiceData> services;
    private final Context context;

    public StationAdapter(Context context, ArrayList<ServiceData> services) {
        this.context = context;
        this.services = services;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ServiceHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.departures, viewGroup, false);

        return new ServiceHolder(view, context);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ServiceHolder viewHolder, final int position) {}

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return services.size();
    }
}
