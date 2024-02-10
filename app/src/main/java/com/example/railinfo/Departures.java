package com.example.railinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.net.ssl.HttpsURLConnection;

public class Departures extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departures);
        RecyclerView recyclerView = findViewById(R.id.RecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<ServiceData> services = getServices("EXD");
        StationAdapter adapter = new StationAdapter(this, services);
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<ServiceData> getServices(String crs) {

        ArrayList<ServiceData> services = new ArrayList<ServiceData>();
        ExecutorService pool = Executors.newFixedThreadPool(3);
        Callable<JSONObject> callable = new ServicesAPI();
        Future<JSONObject> future = pool.submit(callable);
        JSONObject json = null;
        try {
            json = future.get();
            if (json != null) {
                JSONArray servicesJson = json.getJSONArray("trainServices");

                // Create Service Data objects and return services array list
                for (int i = 0; i < servicesJson.length(); i++) {
                    services.add(new ServiceData(servicesJson.getJSONObject(i)));
                }
            }
        } catch (JSONException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return services;
    }
}