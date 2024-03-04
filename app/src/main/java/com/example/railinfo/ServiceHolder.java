package com.example.railinfo;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ServiceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final Context context;
    private final TextView title;
    private final TextView subtitle;
    private final TextView time;
    private final TextView status;
    private final TextView platform;
    private final TextView dash;
    private final TextView journey_time;
    private final TextView operator;
    private ServiceData service = null;

    public ServiceHolder(View view, Context context) {
        super(view);
        view.setOnClickListener(this);
        this.context = context;
        title = view.findViewById(R.id.txt_destination);
        subtitle = view.findViewById(R.id.txt_via);
        time = view.findViewById(R.id.txt_departure);
        status= view.findViewById(R.id.txt_status);
        platform = view.findViewById(R.id.txt_platform_no);
        dash = view.findViewById(R.id.txt_dash);
        journey_time = view.findViewById(R.id.txt_journey_time);
        operator = view.findViewById(R.id.txt_operator);
   }

   @Override
    public void onClick(View view) {
        System.out.println("Clicked");
        if (service != null) {
            Intent intent = new Intent(context, ServiceInfoActivity.class);
            intent.putExtra("service", service.toString());
            context.startActivity(intent);
        } else {
            System.out.println("Service is null");
        }
    }

    public void setService(ServiceData service) {
        this.service = service;
   }

    public void setStation(String name) {
        title.setText(name);
    }

    public void setSubtext(String text) {
        subtitle.setVisibility(View.VISIBLE);
        subtitle.setText(text);
    }

    public void removeSubtext() {
        subtitle.setVisibility(View.GONE);
    }

    public void setTime(String time_str) {
        time.setText(time_str);
    }

    public void setCancelled() {
        status.setText(context.getString(R.string.cancelled));
        status.setTextColor(context.getColor(R.color.cancelled));
    }

    public void setDelayed(String exp_time) {
        status.setText(context.getString(R.string.exp, exp_time));
        status.setTextColor(context.getColor(R.color.delayed));
    }

    public void setOnTime() {
        status.setText(context.getString(R.string.on_time));
        status.setTextColor(context.getColor(R.color.on_time));
    }

    public void setPlatform(String platform_no) {
        platform.setText(platform_no);
    }

    public void setOperator(String name) {
        operator.setText(name);
    }

    public void setJourneyTime(String time) {
        journey_time.setText(time);
        journey_time.setVisibility(View.VISIBLE);
        dash.setVisibility(View.VISIBLE);
    }

    public void removeJourneyTime() {
        dash.setVisibility(View.GONE);
        journey_time.setVisibility(View.GONE);
    }
}

