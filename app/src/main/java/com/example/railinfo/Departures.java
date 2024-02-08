package com.example.railinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class Departures extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departures);
        System.out.println("Output");
        RecyclerView recyclerView = findViewById(R.id.RecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<ServiceData> services = getServices("EXD");
        StationAdapter adapter = new StationAdapter(this, services);
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<ServiceData> getServices(String crs) {
        System.out.println(BuildConfig.RailAPIKey);

        ArrayList<ServiceData> services = new ArrayList<ServiceData>();
        for (int i = 0; i<10; i++) {
            services.add(new ServiceData());
        }
        return services;
    }
}