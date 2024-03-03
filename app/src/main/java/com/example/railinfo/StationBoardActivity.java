package com.example.railinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class StationBoardActivity extends AppCompatActivity {
    private String crs = "";
    private String station = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_board);

        Intent intent = getIntent();
        crs = intent.getStringExtra("crs");
        station = intent.getStringExtra("station");

        TextView title = findViewById(R.id.txt_station_name);
        title.setText(station);

        RecyclerView recyclerView = findViewById(R.id.departures_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<ServiceData> services = getServices();
        DepartureAdapter adapter = new DepartureAdapter(this, services);
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<ServiceData> getServices() {

        ArrayList<ServiceData> services = new ArrayList<>();
        ExecutorService pool = Executors.newFixedThreadPool(3);
        Callable<JSONObject> callable = new StationBoardAPI(crs);
        Future<JSONObject> future = pool.submit(callable);
        JSONObject json;
        try {
            json = future.get();
            if (json != null) {
                JSONArray servicesJson = json.getJSONArray("trainServices");

                // Create Service Data objects and return services array list
                for (int i = 0; i < servicesJson.length(); i++) {
                    ServiceData service = new ServiceData(servicesJson.getJSONObject(i), station, crs);
                    if (service.isDeparture()) {
                        services.add(service);
                    }
                }
            }
        } catch (JSONException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return services;
    }
}