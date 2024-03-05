package com.example.railinfo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.railinfo.R;
import com.example.railinfo.data.objects.HistoryData;
import com.example.railinfo.viewholders.HistoryHolder;

import java.util.ArrayList;

/**
 * An adapter for showing the previous station searches of a user on the SelectStationActivity.
 *
 * @author Sam Townley
 * @version 1.0
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryHolder>{

    /** An array of HistoryData objects representing previous station searches */
    private final ArrayList<HistoryData> history;

    /** The context for the activity that the adapter belongs to. */
    private final Context context;

    /**
     * Creates a new instance of the HistoryAdapter with the specified dataset.
     *
     * @param context The context of the activity that the adapter belongs to.
     * @param history An ArrayList of HistoryData objects representing entries in the user's
     *                stored history.
     */
    public HistoryAdapter(Context context, ArrayList<HistoryData> history) {
        this.context = context;
        this.history = history;
    }

    /**
     * Creates a new ViewHolder for adding a new view onto the RecyclerView. Automatically
     * invoked by the layout manager.
     *
     * @param viewGroup The ViewGroup into which the new View will be added after it is bound to
     *                  an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return The HistoryHolder containing the methods for adding data to the View.
     */
    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Inflate the history.xml layout, used for all history entries
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.history, viewGroup, false);

        return new HistoryHolder(view, context);
    }

    /**
     * Replaces the contents of a view within the RecyclerView as it's moved up and down.
     * Invoked automatically by the layout manager.
     * <p>
     * Binds the information about previous station searches, such as the name and date it was
     * last accessed to the ViewHolder.
     *
     * @param viewHolder The HistoryHolder which should be updated to represent the contents of the
     *                   item at the given position in the data set.
     * @param position The position of the item within the dataset.
     */
    @Override
    public void onBindViewHolder(@NonNull HistoryHolder viewHolder, final int position) {
        // Get the HistoryData object based on the position parameter
        HistoryData data = history.get(position);

        // Set the TextViews of the ViewHolder with the history entry
        viewHolder.setName(data.getStation());
        viewHolder.setCrs(data.getCrs());
        viewHolder.setStation();
        viewHolder.setTime(data.getTime());
        viewHolder.setDate(data.getDate());

        // Set a full border if it's the last position in the array, otherwise set a partial
        // border, which leaves the bottom side open.
        if (position == (history.size() - 1)) {
            viewHolder.setFullBorder();
        } else {
            viewHolder.setPartialBorder();
        }
    }

    /**
     * Returns the number of history entries held by the user.
     * Invoked by the layout manager.
     *
     * @return The size of the dataset.
     */
    @Override
    public int getItemCount() {
        return history.size();
    }
}
