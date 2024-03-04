package com.example.railinfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryHolder>{
    private final ArrayList<HistoryData> history;
    private final Context context;

    public HistoryAdapter(Context context, ArrayList<HistoryData> history) {
        this.context = context;
        this.history = history;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.history, viewGroup, false);

        return new HistoryHolder(view, context);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull HistoryHolder viewHolder, final int position) {
        HistoryData data = history.get(position);
        viewHolder.setName(data.getStation());
        viewHolder.setCrs(data.getCrs());
        viewHolder.setStation();
        viewHolder.setTime(data.getTime());
        viewHolder.setDate(data.getDate());
        if (position == (history.size() - 1)) {
            viewHolder.setFullBorder();
        } else {
            viewHolder.setPartialBorder();
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return history.size();
    }
}
