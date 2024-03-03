package com.example.railinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SelectStationActivity extends AppCompatActivity {

    private static JSONObject stations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_station);

        stations = loadStations();
        List<String> names_list = getStationNames();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, names_list);

        AutoCompleteTextView textView = findViewById(R.id.enter_station_autocomplete);
        textView.setAdapter(adapter);
        textView.setOnItemClickListener(new AutoCompleteListener());
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
        JSONObject inverted_stations = stations.names().toJSONObject(station_names);
        return inverted_stations.getString(station);
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