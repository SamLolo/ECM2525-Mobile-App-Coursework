package com.example.railinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.railinfo.data.api.ArrivalDepartureAPI;
import com.example.railinfo.data.objects.ServiceData;
import com.example.railinfo.fragments.ArrivalsFragment;
import com.example.railinfo.fragments.DeparturesFragment;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * An activity to display the arrivals and departures at a specified station.
 * <p>
 * Requires a station CRS code and station name to passed through the intent in order
 * to load correctly.
 *
 * @author Sam Townley
 * @version 1.1
 */
public class StationBoardActivity extends AppCompatActivity {

    /**
     * The array of ServiceData objects to render on this page.
     * Loaded during activity creation.
     */
    private static ArrayList<ServiceData> services = new ArrayList<>();

    /**
     * Called when creating a new instance of this activity.
     * <p>
     * Sets the content view and configures the views.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}, otherwise null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Create the activity and set the content layout
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
                .replace(R.id.fragments_container, DeparturesFragment.class, null)
                .commit();

        ImageButton info = findViewById(R.id.btn_station_info);
        info.setOnClickListener(v -> {
            Intent info_intent = new Intent(this, StationInfoActivity.class);
            info_intent.putExtra("crs", crs);
            startActivity(info_intent);
        });

        ImageButton back = findViewById(R.id.btn_back);
        back.setOnClickListener(v -> finish());

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (Objects.equals(tab.getText(), "Departures")) {
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragments_container, DeparturesFragment.class, null)
                            .commit();
                } else {
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragments_container, ArrivalsFragment.class, null)
                            .commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    /**
     * Adds a new entry to the users search history.
     * Called once when the activity is created for a new CRS code.
     *
     * @param crs The CRS code to add to the users history.
     */
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

    /**
     *
     * @param crs The CRS code to load services for.
     * @param station The name of the station this CRS code relates to.
     * @return An ArrayList containing all services fetched from the API.
     */
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

    /**
     * Static method to get the services array held by this activity.
     * Used by the fragments to get the services they need to display.
     *
     * @return An ArrayList of services that were loaded from the API.
     */
    public static ArrayList<ServiceData> getServices() {
        return services;
    }
}