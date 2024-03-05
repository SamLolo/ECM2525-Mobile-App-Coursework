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

public class HistoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final Context context;
    private final TextView station;
    private final TextView time;
    private final TextView date;
    private final ConstraintLayout layout;
    private String name;
    private String crs;
    public HistoryHolder(View view, Context context) {
        super(view);
        view.setOnClickListener(this);
        this.context = context;
        layout = view.findViewById(R.id.history_layout);
        station = view.findViewById(R.id.history_station);
        time = view.findViewById(R.id.history_time);
        date = view.findViewById(R.id.history_date);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(context, StationBoardActivity.class);
        intent.putExtra("crs", crs);
        intent.putExtra("station", name);
        context.startActivity(intent);
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

