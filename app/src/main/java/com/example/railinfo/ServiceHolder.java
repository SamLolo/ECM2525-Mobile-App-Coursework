package com.example.railinfo;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Locale;

public class ServiceHolder extends RecyclerView.ViewHolder {
    private final Context context;
    private final TextView title_main;
    private final TextView title_secondary;
    private final TextView subtitle;
    private final TextView departure_time;
    private final TextView status;
    private final TextView platform;
    private final TextView dash1;
    private final TextView dash2;
    private final TextView journey_time;
    private final TextView coaches;
    private final TextView operator;


    public ServiceHolder(View view, Context context) {
        super(view);
        this.context = context;
        title_main = view.findViewById(R.id.txt_destination);
        title_secondary = view.findViewById(R.id.txt_destination_with_via);
        subtitle = view.findViewById(R.id.txt_via);
        departure_time = view.findViewById(R.id.txt_depature);
        status = view.findViewById(R.id.txt_status);
        platform = view.findViewById(R.id.txt_platform_no);
        dash1 = view.findViewById(R.id.txt_dash_1);
        dash2 = view.findViewById(R.id.txt_dash_2);
        journey_time = view.findViewById(R.id.txt_time);
        coaches = view.findViewById(R.id.txt_length);
        operator = view.findViewById(R.id.txt_operator);
   }
    public void setDestination(String name) {
        title_main.setText(name);
    }

    public void setDestinationWithVia(String name, String via) {
        title_secondary.setText(name);
        subtitle.setText(context.getString(R.string.via, via));
    }

    public void setDepartureTime(String time) {
        departure_time.setText(time);
    }

    public void setCancelled() {
        status.setText(context.getString(R.string.cancelled));
        int colour = context.getColor(R.color.cancelled);
        status.setTextColor(colour);
    }

    public void setDelayed(String exp_time) {
        status.setText(context.getString(R.string.exp, exp_time));
        int colour = context.getColor(R.color.delayed);
        status.setTextColor(colour);
    }

    public void setPlatform(Integer platform_num) {
        platform.setText(String.format(Locale.getDefault(), "%d", platform_num));
    }

    public void setOperator(String name) {
        operator.setText(context.getString(R.string.operated_by, name));
    }

    public void setFormationLength(Integer length) {
        coaches.setText(context.getString(R.string.coaches, length));
        dash2.setVisibility(View.VISIBLE);
        coaches.setVisibility(View.VISIBLE);
    }

    public void removeFormationLength() {
        dash2.setVisibility(View.GONE);
        coaches.setVisibility(View.GONE);
    }

    public void removeJourneyTime() {
        dash1.setVisibility(View.GONE);
        journey_time.setVisibility(View.GONE);
    }
}

