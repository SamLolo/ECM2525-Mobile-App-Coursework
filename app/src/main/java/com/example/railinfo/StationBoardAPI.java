package com.example.railinfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.concurrent.Callable;

import javax.net.ssl.HttpsURLConnection;

public class StationBoardAPI implements Callable<JSONObject> {
    private final String crs;

    public StationBoardAPI (String crs) {
        this.crs = crs;
    }

    @Override
    public JSONObject call() {
        StringBuilder response = new StringBuilder();
        try {
            // Create HTTPS connection to Rail Data API
            String uri = String.format(Locale.US, "https://api1.raildata.org.uk/1010-live-arrival-and-departure-boards-arr-and-dep/LDBWS/api/20220120/GetArrDepBoardWithDetails/%s", crs);
            URL url = new URL(uri);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("x-apikey", BuildConfig.RailAPIKey);
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println(connection.getResponseMessage());
                return null;
            }

            // Read JSON content from API into String
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        // Convert response object into JSON
        JSONObject json;
        try {
            json = new JSONObject(response.toString());
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
