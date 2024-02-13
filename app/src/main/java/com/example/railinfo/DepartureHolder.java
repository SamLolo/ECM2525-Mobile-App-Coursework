package com.example.railinfo;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class DepartureHolder extends RecyclerView.ViewHolder {
    private final Context context;
    private final TextView title_main;
    private final TextView title_secondary;
    private final TextView subtitle;
    private final TextView time_view;
    private final TextView status_view;
    private final TextView platform_view;
    private final TextView dash1;
    private final TextView dash2;
    private final TextView sub_info1;
    private final TextView sub_info2;
    private final TextView sub_info3;


    public DepartureHolder(View view, Context context) {
        super(view);
        this.context = context;
        title_main = view.findViewById(R.id.txt_destination);
        title_secondary = view.findViewById(R.id.txt_destination_with_via);
        subtitle = view.findViewById(R.id.txt_via);
        time_view = view.findViewById(R.id.txt_departure);
        status_view = view.findViewById(R.id.txt_status);
        platform_view = view.findViewById(R.id.txt_platform_no);
        dash1 = view.findViewById(R.id.txt_dash_1);
        dash2 = view.findViewById(R.id.txt_dash_2);
        sub_info1 = view.findViewById(R.id.txt_subinfo_1);
        sub_info2 = view.findViewById(R.id.txt_subinfo_2);
        sub_info3 = view.findViewById(R.id.txt_subinfo_3);
   }
    public void setDestination(String name) {
        title_main.setText(name);
    }

    public void setDestinationWithVia(String name, String via) {
        title_secondary.setText(name);
        subtitle.setText(via);
    }

    public void setTime(String time) {
        time_view.setText(time);
    }

    public void setCancelled() {
        status_view.setText(context.getString(R.string.cancelled));
        int colour = context.getColor(R.color.cancelled);
        status_view.setTextColor(colour);
    }

    public void setDelayed(String exp_time) {
        status_view.setText(context.getString(R.string.exp, exp_time));
        int colour = context.getColor(R.color.delayed);
        status_view.setTextColor(colour);
    }

    public void setPlatform(String platform) {
        platform_view.setText(platform);
    }

    public void setOperator(String name) {
        sub_info3.setText(context.getString(R.string.operated_by, name));
        if (sub_info3.getVisibility() != View.VISIBLE) {
            addInfo3();
        }
    }

    public void setFormationLength(Integer length) {
        sub_info2.setText(context.getString(R.string.coaches, length));
        if (sub_info2.getVisibility() != View.VISIBLE) {
            addInfo2();
        }
    }

    public void setJourneyTime(String time) {
        sub_info1.setText(time);
        if (sub_info1.getVisibility() != View.VISIBLE) {
            addInfo1();
        }
    }

    public void addInfo1() {
        dash1.setVisibility(View.VISIBLE);
        sub_info1.setVisibility(View.VISIBLE);
    }

    public void addInfo2() {
        dash2.setVisibility(View.VISIBLE);
        sub_info2.setVisibility(View.VISIBLE);
    }

    public void addInfo3() {
        dash2.setVisibility(View.VISIBLE);
        sub_info3.setVisibility(View.VISIBLE);
    }

    public void removeInfo1() {
        dash1.setVisibility(View.GONE);
        sub_info1.setVisibility(View.GONE);
    }

    public void removeInfo2() {
        dash2.setVisibility(View.GONE);
        sub_info2.setVisibility(View.GONE);
    }

    public void removeInfo3() {
        dash2.setVisibility(View.GONE);
        sub_info3.setVisibility(View.GONE);
    }
}

