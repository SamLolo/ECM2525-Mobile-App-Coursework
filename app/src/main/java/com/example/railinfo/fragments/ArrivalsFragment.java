package com.example.railinfo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.railinfo.R;
import com.example.railinfo.data.objects.ServiceData;
import com.example.railinfo.StationBoardActivity;
import com.example.railinfo.adapters.ArrivalsAdapter;

import java.util.ArrayList;

public class ArrivalsFragment extends Fragment {

    public ArrivalsFragment() {
        super(R.layout.station_board_recycler_view);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.station_board_recycler_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        RecyclerView recyclerView = view.findViewById(R.id.departures_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<ServiceData> services = StationBoardActivity.getServices();
        ArrayList<ServiceData> arrivals = new ArrayList<>();
        for (int i=0; i < services.size(); i++) {
            ServiceData service = services.get(i);
            if (service.isArrival()) {
                arrivals.add(service);
            }
        }
        ArrivalsAdapter adapter = new ArrivalsAdapter(this.getContext(), arrivals);
        recyclerView.setAdapter(adapter);
    }
}