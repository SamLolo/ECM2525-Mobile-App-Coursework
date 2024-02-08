package com.example.railinfo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StationAdapter extends RecyclerView.Adapter<StationAdapter.ViewHolder> {
    private ArrayList<ServiceData> services;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView destination;
        private final TextView destination_via;
        private final TextView via;
        private final TextView departure_time;

        public ViewHolder(View view) {
            super(view);
            destination = view.findViewById(R.id.txt_destination);
            destination_via = view.findViewById(R.id.txt_destination_with_via);
            via = view.findViewById(R.id.txt_via);
            departure_time = view.findViewById(R.id.txt_depature);
        }

        public void setDestination(String name) {
            destination.setText(name);
        }
    }

    public StationAdapter(ArrayList<ServiceData> services) {
        this.services = services;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.departures, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {}

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return services.size();
    }
}
