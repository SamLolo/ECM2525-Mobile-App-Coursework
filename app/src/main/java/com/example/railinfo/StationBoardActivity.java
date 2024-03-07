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

        // Get CRS code and station name to load the station board for
        Intent intent = getIntent();
        String crs = intent.getStringExtra("crs");
        String station = intent.getStringExtra("station");

        // Add to the user's history and load the services for the crs code
        addHistory(crs);
        services = loadServices(crs, station);

        // Set the name of the station as the title of the window
        TextView title = findViewById(R.id.txt_station_name);
        title.setText(station);

        // Add the departures fragment as the default fragment to display.
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragments_container, DeparturesFragment.class, null)
                .commit();

        // Make it so that the StationInfoActivity is explicitly started on info button click
        ImageButton info = findViewById(R.id.btn_station_info);
        info.setOnClickListener(v -> {
            Intent info_intent = new Intent(this, StationInfoActivity.class);
            info_intent.putExtra("crs", crs);
            startActivity(info_intent);
        });

        // Finish activity if the back button is clicked
        ImageButton back = findViewById(R.id.btn_back);
        back.setOnClickListener(v -> finish());

        // Listen for a tab being changed
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            /**
             * Called automatically when a new tab is selected within the tab layout.
             *
             * @param tab The tab that was selected
             */
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // If departures tab is selected, replace fragment container with DeparturesFragment
                if (Objects.equals(tab.getText(), "Departures")) {
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragments_container, DeparturesFragment.class, null)
                            .commit();

                // If arrivals tab is selected, replace fragment container with ArrivalsFragment
                } else {
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragments_container, ArrivalsFragment.class, null)
                            .commit();
                }
            }

            /**
             * Called when a tab within the layout is unselected.
             * Will always be called alongside {@link #onTabSelected(TabLayout.Tab)}.
             * <p>
             * This method must be implemented by the OnTabSelectedListener but doesn't need
             * to provide any functionality.
             *
             * @param tab The tab that was unselected
             */
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            /**
             * Called when a tab that is already selected is reselected by the user.
             * <p>
             * This method must be implemented by the OnTabSelectedListener but doesn't need
             * to provide any functionality.
             *
             * @param tab The tab that was reselected.
             */
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
        // Get an editor instance of the history SharedPreferences
        SharedPreferences pf = getSharedPreferences("history", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pf.edit();

        // Load and parse the current history into a JSONObject
        JSONObject history;
        try {
            history = new JSONObject(pf.getString("history", "{}"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        // Add the new entry with the current timestamp
        DateFormat df = DateFormat.getDateTimeInstance();
        try {
            // If CRS not already in history, remove an entry if there are already 8
            if (history.isNull(crs)) {
                if (history.length() >= 8) {
                    String to_remove = history.keys().next();
                    history.remove(to_remove);
                }
                history.putOpt(crs, df.format(Calendar.getInstance().getTime()));

            // Otherwise make sure to replace the current CRS by removing it first
            } else {
                history.remove(crs);
                history.putOpt(crs, df.format(Calendar.getInstance().getTime()));
            }

        // Handle any JSONExceptions that occur whilst altering the history
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        // Save the new history json as a string
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
        // Get a JSONObject of services by carrying out an API task off the main thread.
        ArrayList<ServiceData> services = new ArrayList<>();
        ExecutorService pool = Executors.newFixedThreadPool(3);
        Callable<JSONObject> callable = new ArrivalDepartureAPI(crs);
        Future<JSONObject> future = pool.submit(callable);

        // Check the JSONObject returned has train services inside of it
        JSONObject json;
        try {
            json = future.get();
            if (json != null) {
                JSONArray servicesJson = json.getJSONArray("trainServices");

                // Create Service Data objects and return services array list
                for (int i = 0; i < servicesJson.length(); i++) {
                    services.add(new ServiceData(servicesJson.getJSONObject(i), station, crs));                }
            }

        // Handle any exceptions that occur whilst carrying out the API task or parsing the data
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