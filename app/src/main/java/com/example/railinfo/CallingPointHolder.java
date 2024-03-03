package com.example.railinfo;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

public class CallingPointHolder extends RecyclerView.ViewHolder{
    private final Context context;
    private final TextView time;
    private final TextView status;
    private final TextView station;
    private final TextView platform;
    private final View line_1;
    private final View line_2;
    private final View circle;
    private Boolean visited = false;

    public CallingPointHolder(View view, Context context) {
        super(view);
        this.context = context;
        time = view.findViewById(R.id.calling_point_time);
        status = view.findViewById(R.id.calling_point_status);
        station = view.findViewById(R.id.calling_point_name);
        platform = view.findViewById(R.id.calling_point_platform);
        line_1 = view.findViewById(R.id.line_1);
        line_2 = view.findViewById(R.id.line_2);
        circle = view.findViewById(R.id.circle);
    }

    public void setVisited() {
        line_1.setBackgroundColor(context.getColor(R.color.visited));
        line_2.setBackgroundColor(context.getColor(R.color.visited));

        Resources res = context.getResources();
        circle.setBackground(ResourcesCompat.getDrawable(res, R.drawable.filled_circle, null));

        visited = true;
    }

    public void setToVisit() {
        line_1.setBackgroundColor(context.getColor(R.color.to_visit));
        line_2.setBackgroundColor(context.getColor(R.color.to_visit));

        Resources res = context.getResources();
        circle.setBackground(ResourcesCompat.getDrawable(res, R.drawable.circle_outline, null));

        visited = false;
    }

    public void setTime(String departure) {
        time.setText(departure);
    }

    public void setStation(String name) {
        station.setText(name);
    }

    public void setPlatform(Integer platform_no) {
        platform.setText(context.getString(R.string.platform_with_no, platform_no));
    }

    public void setAsOrigin() {
        line_1.setVisibility(View.INVISIBLE);
        line_2.setVisibility(View.VISIBLE);
    }

    public void setAsDestination() {
        line_1.setVisibility(View.VISIBLE);
        line_2.setVisibility(View.INVISIBLE);
    }

    public void setNormal() {
        line_1.setVisibility(View.VISIBLE);
        line_2.setVisibility(View.VISIBLE);
    }

    public void setCancelled() {
        status.setText(context.getString(R.string.cancelled));
        status.setTextColor(context.getColor(R.color.cancelled));
    }

    public void setDelayed(String time) {
        status.setTextColor(context.getColor(R.color.delayed));
        if (!visited) {
            status.setText(context.getString(R.string.expected, time));
        } else {
            status.setText(context.getString(R.string.delayed_departure, time));
        }
    }

    public void setOnTime() {
        status.setTextColor(context.getColor(R.color.on_time));
        if (!visited) {
            status.setText(context.getString(R.string.on_time));
        } else {
            status.setText(context.getString(R.string.on_time_departure));
        }
    }
}
