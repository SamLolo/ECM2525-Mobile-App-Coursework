package com.example.railinfo;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryHolder extends RecyclerView.ViewHolder {
    private final Context context;
    private final TextView station;
    private final TextView time;
    private final TextView date;
    private final ConstraintLayout layout;
    private String name;
    private String crs;
    public HistoryHolder(View view, Context context) {
        super(view);
        this.context = context;
        layout = view.findViewById(R.id.history_layout);
        station = view.findViewById(R.id.history_station);
        time = view.findViewById(R.id.history_time);
        date = view.findViewById(R.id.history_date);
    }

    public void setStation() {
        station.setText(context.getString(R.string.station_with_crs, name, crs));
    }

    public void setTime(String time_str) {
        time.setText(time_str);
    }

    public void setDate(String date_str) {
        date.setText(date_str);
    }

    public void setPartialBorder() {
        Resources res = context.getResources();
        layout.setBackground(ResourcesCompat.getDrawable(res, R.drawable.open_base_border, null));
    }

    public void setFullBorder() {
        Resources res = context.getResources();
        layout.setBackground(ResourcesCompat.getDrawable(res, R.drawable.flat_border, null));
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCrs(String crs) {
        this.crs = crs;
    }
}
