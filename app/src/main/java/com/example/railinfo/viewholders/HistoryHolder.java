package com.example.railinfo.viewholders;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.railinfo.R;
import com.example.railinfo.StationBoardActivity;

import java.util.Objects;

/**
 * Defines the structure of each view within the History RecyclerView and the methods to
 * control what data is displayed on the view.
 * <p>
 * Used by the {@link com.example.railinfo.adapters.HistoryAdapter}.
 *
 * @author Sam Townley
 * @version 1.0
 */
public class HistoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    /** The context for the activity that the view holder belongs to. */
    private final Context context;

    /** A text view displaying the name of station. */
    private final TextView station;

    /** A text view displaying the time the search was last made. **/
    private final TextView time;

    /** A text view displaying the date the search was last made. **/
    private final TextView date;

    /** The layout (background) storing all of the text views. **/
    private final ConstraintLayout layout;

    /** The name of the station the history entry relates to. */
    private String name = "";

    /** The Computer-Reservation System (CRS) code of the station above. */
    private String crs = "";

    /**
     * Constructs a new view holder for the editing the contents of a history view.
     *
     * @param view The view that was inflated from the layout by the HistoryAdapter.
     * @param context The context for the activity that the view holder belongs to.
     */
    public HistoryHolder(View view, Context context) {
        // Initialise the parent view
        super(view);

        // Set the on click listener to be this class so the onClick() method is used.
        view.setOnClickListener(this);
        this.context = context;

        // Fetch the necessary views from the layout
        layout = view.findViewById(R.id.history_layout);
        station = view.findViewById(R.id.history_station);
        time = view.findViewById(R.id.history_time);
        date = view.findViewById(R.id.history_date);
    }

    /**
     * Called when this view holder has been clicked.
     * <p>
     * Starts the StationBoardActivity with the station name and crs stored by this view holder
     * at the time it was clicked.
     *
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        // Check that the name and crs have been correctly set before calling the intent
        if (!Objects.equals(name, "") && !Objects.equals(crs, "")) {

            // Start the StationBoardActivity using an explicit intent
            Intent intent = new Intent(context, StationBoardActivity.class);
            intent.putExtra("crs", crs);
            intent.putExtra("station", name);
            context.startActivity(intent);

        // Warn the console if the either of the data entries is null
        } else {
            System.out.println("History view holder has no data!");
        }
    }

    /**
     * Sets the name of the station using the stored name and crs values.
     */
    public void setStation() {
        station.setText(context.getString(R.string.station_with_crs, name, crs));
    }

    /**
     * Sets the time the history entry was last accessed.
     *
     * @param time_str The time, formatted as a string in the 24-hour clock.
     */
    public void setTime(String time_str) {
        time.setText(time_str);
    }

    /**
     * Sets the date the history entry was last accessed.
     *
     * @param date_str The date, formatted as a string.
     */
    public void setDate(String date_str) {
        date.setText(date_str);
    }

    /**
     * Sets the background to have a partial border, i.e a border with an open bottom.
     */
    public void setPartialBorder() {
        Resources res = context.getResources();
        layout.setBackground(ResourcesCompat.getDrawable(res, R.drawable.open_base_border, null));
    }

    /**
     * Sets the background to have a full border.
     * Used for the last entry displayed by the RecyclerView.
     */
    public void setFullBorder() {
        Resources res = context.getResources();
        layout.setBackground(ResourcesCompat.getDrawable(res, R.drawable.flat_border, null));
    }

    /**
     * Sets the name of the station displayed by this view holder.
     *
     * @param name The name of the station.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the CRS code of the station displayed by this view holder.
     *
     * @param crs The CRS code of the station.
     */
    public void setCrs(String crs) {
        this.crs = crs;
    }
}

