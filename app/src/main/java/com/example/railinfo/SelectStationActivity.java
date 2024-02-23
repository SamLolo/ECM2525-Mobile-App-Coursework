package com.example.railinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SelectStationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_station);

        AssetManager assets = getAssets();
        StringBuilder json = new StringBuilder();
        try {
            BufferedReader file = new BufferedReader(new InputStreamReader(assets.open("stations.json")));
            String line = null;
            while ((line = file.readLine()) != null) {
                json.append(line);
            }
            file.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Unable to load stations!");
        }

        JSONObject stations;
        try {
            stations = new JSONObject(json.toString());
        } catch (JSONException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Unable to load stations!");
        }

        List<String> names_list = new ArrayList<>();
        JSONArray names;
        try {
            names = stations.toJSONArray(stations.names());
        } catch (JSONException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Unable to load stations!");
        }

        if (names != null) {
            for (int i = 0; !names.isNull(i); i++) {
                names_list.add(names.optString(i));
            }
        } else {
            throw new RuntimeException("Unable to load stations!");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, names_list);

        AutoCompleteTextView textView = (AutoCompleteTextView)findViewById(R.id.enter_station_autocomplete);
        textView.setAdapter(adapter);
    }
}