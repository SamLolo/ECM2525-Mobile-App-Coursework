package com.example.railinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class StationBoardActivity extends AppCompatActivity {

    private static ArrayList<ServiceData> services = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_board);

        Intent intent = getIntent();
        String crs = intent.getStringExtra("crs");
        String station = intent.getStringExtra("station");

        addHistory(crs);
        services = loadServices(crs, station);

        TextView title = findViewById(R.id.txt_station_name);
        title.setText(station);

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragments_container, ArrivalsFragment.class, null)
                .commit();

        ImageButton info = findViewById(R.id.btn_station_info);
        info.setOnClickListener(v -> {
            Intent info_intent = new Intent(this, StationInfoActivity.class);
            info_intent.putExtra("crs", crs);
            startActivity(info_intent);
        });
    }

    private void addHistory(String crs) {
        SharedPreferences pf = getSharedPreferences("history", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pf.edit();
        JSONObject history;
        try {
            history = new JSONObject(pf.getString("history", "{}"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        DateFormat df = DateFormat.getDateTimeInstance();
        try {
            if (history.isNull(crs)) {
                if (history.length() >= 8) {
                    String to_remove = history.keys().next();
                    history.remove(to_remove);
                }
                history.putOpt(crs, df.format(Calendar.getInstance().getTime()));
            } else {
                history.remove(crs);
                history.putOpt(crs, df.format(Calendar.getInstance().getTime()));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        editor.putString("history", history.toString());
        editor.apply();
    }

    public ArrayList<ServiceData> loadServices(String crs, String station) {

        ArrayList<ServiceData> services = new ArrayList<>();
        ExecutorService pool = Executors.newFixedThreadPool(3);
        Callable<JSONObject> callable = new ArrivalDepartureAPI(crs);
        Future<JSONObject> future = pool.submit(callable);
        JSONObject json;
        try {
            json = future.get();
            if (json != null) {
                JSONArray servicesJson = json.getJSONArray("trainServices");

                // Create Service Data objects and return services array list
                for (int i = 0; i < servicesJson.length(); i++) {
                    services.add(new ServiceData(servicesJson.getJSONObject(i), station, crs));                }
            }
        } catch (JSONException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return services;
    }

    public static ArrayList<ServiceData> getServices() {
        return services;
    }
}