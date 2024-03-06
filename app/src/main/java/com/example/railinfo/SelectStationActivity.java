package com.example.railinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.example.railinfo.adapters.HistoryAdapter;
import com.example.railinfo.data.objects.HistoryData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * The main launcher activity of the application.
 * <p>
 * Allows the user to select the station they'd like to see the arrivals/departure board for.
 *
 * @author Sam Townley
 * @version 1.2
 */
public class SelectStationActivity extends AppCompatActivity {

    /** The HistoryData entries for the user, accessed by the HistoryAdapter. */
    private final ArrayList<HistoryData> history = new ArrayList<>();

    /** The adapter being used to display the users search history within the RecyclerView. */
    private HistoryAdapter hAdapter;

    /** A JSONObject mapping every stations CRS code to it's name. */
    private JSONObject stations;

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
        setContentView(R.layout.activity_select_station);

        // Setup the toolbar as an ActionBar and hide the automatic title
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Load stations and get a list of station names from the JSON file
        stations = loadStations();
        List<String> names_list = getStationNames();

        // Create the adapter and assign it to the AutoCompleteTextView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, names_list);
        AutoCompleteTextView input = findViewById(R.id.enter_station_autocomplete);
        input.setAdapter(adapter);
        input.setOnItemClickListener(new AutoCompleteListener());

        // Setup the history RecyclerView using the HistoryAdapter
        RecyclerView history_view = findViewById(R.id.history_view);
        history_view.setLayoutManager(new LinearLayoutManager(this));
        loadHistory();

        // Save the adapter as a class attribute so it can be notified when the data changes
        hAdapter = new HistoryAdapter(this, history);
        history_view.setAdapter(hAdapter);
    }

    /**
     * Called when the activity is being restarted.
     * <p>
     * This will be called when the user navigates back to this activity.
     * It allows me to refresh the user history before displaying the content.
     */
    @Override
    protected void onRestart() {
        // Reload the history and notify the adapter that the dataset has changed
        loadHistory();
        hAdapter.notifyDataSetChanged();

        // Reset the text inside the AutoCompleteTextView
        AutoCompleteTextView input = findViewById(R.id.enter_station_autocomplete);
        input.setText("");

        // Hide the keyboard by removing the focus on the AutoCompleteTextView
        input.postDelayed(() -> {
            if (getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }, 500);
        super.onRestart();
    }

    /**
     * Initializes the contents of the options menu.
     * This is only called once, the first time the options menu is displayed.
     * It is called automatically by the activity.
     *
     * @param menu The options menu in which you place your items.
     *
     * @return True to show the menu, otherwise false.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the main menu layout and assign it to the menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Called whenever an item in the options menu is selected.
     *
     * @param item The menu item that was selected.
     *
     * @return False to allow normal menu processing to proceed or true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Clear history if selected
        if (item.getItemId() == R.id.clear_history) {
            SharedPreferences pf = getSharedPreferences("history", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pf.edit();
            editor.remove("history");
            history.clear();
            editor.commit();

            // Reload the HistoryAdapter
            hAdapter.notifyDataSetChanged();

            // Make the "no history" text visible again.
            TextView no_history = findViewById(R.id.txt_no_history);
            no_history.setVisibility(View.VISIBLE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Loads the stations stored inside the stations.json file from the assets directory.
     *
     * @return A JSONObject containing the CRS codes and station names.
     */
    public JSONObject loadStations() {

        // Get an instance of the asset manager to access the file
        AssetManager assets = getAssets();

        // Read the lines of the file into a StringBuilder
        StringBuilder json_string = new StringBuilder();
        try {
            BufferedReader file = new BufferedReader(new InputStreamReader(assets.open("stations.json")));
            String line;
            while ((line = file.readLine()) != null) {
                json_string.append(line);
            }
            file.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Unable to load stations!");
        }

        // Convert the JSON string into a JSONObject
        JSONObject json;
        try {
            json = new JSONObject(json_string.toString());
        } catch (JSONException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Unable to load stations!");
        }
        return json;
    }

    /**
     * Gets all the values from the stations JSONObject, which are the station names.
     *
     * @return An array containing the station names.
     * @throws RuntimeException An error occurred whilst trying to invert the JSONObject.
     */
    private ArrayList<String> getStationNames() throws RuntimeException {
        // Get a JSONArray of station names from the JSONObject
        JSONArray names_array;
        try {
            names_array = stations.toJSONArray(stations.names());
        } catch (JSONException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Unable to get station names!");
        }

        // Add the item inside the JSONArray to an ArrayList
        ArrayList<String> names = new ArrayList<>();
        if (names_array != null) {
            for (int i = 0; !names_array.isNull(i); i++) {
                names.add(names_array.optString(i));
            }
        } else {
            throw new RuntimeException("Unable to get station names!");
        }
        return names;
    }

    /**
     * Get the CRS code for a given station name.
     * <p>
     * Used to get the corresponding CRS for the name of the station chosen in the
     * AutoCompleteTextView.
     *
     * @param station The name of the station to get the CRS code for.
     * @return The CRS code of the station.
     * @throws JSONException An exception occurred whilst inverting the JSONObject.
     */
    private String getCrs(String station) throws JSONException {
        // Invert the JSONObject by swapping the keys and values around
        JSONArray station_names = stations.toJSONArray(stations.names());
        JSONObject inverted_stations = Objects.requireNonNull(stations.names()).toJSONObject(station_names);

        // Return the CRS value associated with the station name
        return inverted_stations.getString(station);
    }

    /**
     * Loads a users history from shared preferences.
     * <p>
     * This method converts the history into HistoryData objects and stores it in the history
     * class attribute so it can be accessed by the HistoryAdapter.
     */
    private void loadHistory() {
        // Get the text that displays if a user has history or not
        TextView no_history = findViewById(R.id.txt_no_history);
        SharedPreferences pf = getSharedPreferences("history", Context.MODE_PRIVATE);

        // If the user doesn't have history, make the text visible
        if (!pf.contains("history")) {
            no_history.setVisibility(View.VISIBLE);
        } else {

            // Otherwise, remove the history and read the history from SharedPreferences
            no_history.setVisibility(View.GONE);
            String data = pf.getString("history", "{}");

            // Try to parse the history string into a JSONObject
            try {
                JSONObject json = new JSONObject(data);

                // Add the new entries to the now clear history array
                history.clear();
                for (Iterator<String> it = json.keys(); it.hasNext(); ) {
                    String crs = it.next();
                    history.add(new HistoryData(stations.getString(crs), crs, json.getString(crs)));
                }

                // Sort history into descending date order
                Collections.sort(history);

            // Handle any JSONExceptions or ParseExceptions that occur whilst reading the data
            } catch (JSONException | ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * A listener class used for the AutoCompleteTextView to carry out an action when an option
     * is selected within the dropdown.
     */
    class AutoCompleteListener implements AdapterView.OnItemClickListener {

        /**
         * Starts a new StationBoardActivity using explicit intent when a station has been
         * chosen from the dropdown.
         *
         * @param parent The AdapterView where the click happened.
         * @param view The view within the AdapterView that was clicked (this
         *             will be a view provided by the adapter)
         * @param position The position of the view in the adapter.
         * @param id The row id of the item that was clicked.
         */
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // Get the value of the selected station.
            TextView item = (TextView)view;
            String selected = (String)item.getText();

            // Get the corresponding CRS code for the above station name
            String crs;
            try {
                crs = getCrs(selected);
            } catch (JSONException ex) {
                ex.printStackTrace();
                throw new RuntimeException("Unable to convert station name!");
            }

            // Start the StationBoardActivity, passing in the required CRS code and station name.
            Intent intent = new Intent(SelectStationActivity.this, StationBoardActivity.class);
            intent.putExtra("crs", crs);
            intent.putExtra("station", selected);
            startActivity(intent);
        }
    }
}