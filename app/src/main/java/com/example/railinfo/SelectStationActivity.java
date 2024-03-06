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

public class SelectStationActivity extends AppCompatActivity {
    private final ArrayList<HistoryData> history = new ArrayList<>();
    private HistoryAdapter hAdapter;
    private JSONObject stations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_station);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        stations = loadStations();
        List<String> names_list = getStationNames();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, names_list);
        AutoCompleteTextView input = findViewById(R.id.enter_station_autocomplete);
        input.setAdapter(adapter);
        input.setOnItemClickListener(new AutoCompleteListener());

        RecyclerView history_view = findViewById(R.id.history_view);
        history_view.setLayoutManager(new LinearLayoutManager(this));
        loadHistory();
        hAdapter = new HistoryAdapter(this, history);
        history_view.setAdapter(hAdapter);
    }

    @Override
    protected void onRestart() {
        loadHistory();
        hAdapter.notifyDataSetChanged();

        AutoCompleteTextView input = findViewById(R.id.enter_station_autocomplete);
        input.setText("");

        input.postDelayed(() -> {
            if (getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }, 500);

        super.onRestart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

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

    public JSONObject loadStations() {
        AssetManager assets = getAssets();
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

        JSONObject json;
        try {
            json = new JSONObject(json_string.toString());
        } catch (JSONException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Unable to load stations!");
        }
        return json;
    }

    private ArrayList<String> getStationNames() throws RuntimeException {
        ArrayList<String> names = new ArrayList<>();
        JSONArray names_array;
        try {
            names_array = stations.toJSONArray(stations.names());
        } catch (JSONException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Unable to get station names!");
        }

        if (names_array != null) {
            for (int i = 0; !names_array.isNull(i); i++) {
                names.add(names_array.optString(i));
            }
        } else {
            throw new RuntimeException("Unable to get station names!");
        }
        return names;
    }

    private String getCrs(String station) throws JSONException {
        JSONArray station_names = stations.toJSONArray(stations.names());
        JSONObject inverted_stations = Objects.requireNonNull(stations.names()).toJSONObject(station_names);
        return inverted_stations.getString(station);
    }

    private void loadHistory() {
        TextView no_history = findViewById(R.id.txt_no_history);
        SharedPreferences pf = getSharedPreferences("history", Context.MODE_PRIVATE);
        if (!pf.contains("history")) {
            no_history.setVisibility(View.VISIBLE);
        } else {
            no_history.setVisibility(View.GONE);
            String data = pf.getString("history", "{}");
            try {
                JSONObject json = new JSONObject(data);
                history.clear();
                for (Iterator<String> it = json.keys(); it.hasNext(); ) {
                    String crs = it.next();
                    history.add(new HistoryData(stations.getString(crs), crs, json.getString(crs)));
                }
                Collections.sort(history);
            } catch (JSONException | ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    class AutoCompleteListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TextView item = (TextView)view;
            String selected = (String)item.getText();
            System.out.println(selected);

            String crs;
            try {
                crs = getCrs(selected);
            } catch (JSONException ex) {
                ex.printStackTrace();
                throw new RuntimeException("Unable to convert station name!");
            }
            System.out.println(crs);

            if (crs.equals("")) {
                throw new RuntimeException("Unable to convert station name!");
            } else {
                Intent intent = new Intent(SelectStationActivity.this, StationBoardActivity.class);
                intent.putExtra("crs", crs);
                intent.putExtra("station", selected);
                startActivity(intent);
            }
        }
    }
}