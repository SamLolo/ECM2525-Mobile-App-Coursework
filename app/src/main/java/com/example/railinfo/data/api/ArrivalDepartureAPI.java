package com.example.railinfo.data.api;

import com.example.railinfo.BuildConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.Callable;

import javax.net.ssl.HttpsURLConnection;


/**
 * A callable task for fetching arrivals and departures from the National Rail API.
 * Designed as callable so it can be executed off the main thread by an activity.
 * <p>
 * Uses the Live Arrival and Departure Boards endpoint from the National Rail's Rail Data
 * Marketplace. This endpoint returns information (in json) about the next 10 services to arrive
 * or depart the station specified using the CRS code. For more information, visit
 * <a href="https://raildata.org.uk/dataProduct/P-7c866984-8a7b-4272-a6f8-00b4aaf821fa/overview">https://raildata.org.uk/</a>
 *
 * @author Sam Townley
 * @version 1.0
 */
public class ArrivalDepartureAPI implements Callable<JSONObject> {

    /** The Computer-Reservation System (CRS) code of the station to get services for. */
    private final String crs;

    /**
     * Constructs a new instance of the ArrivalsDepartureAPI callable based on the station
     * crs code passed in.
     *
     * @param crs The crs code of the station to load data for.
     */
    public ArrivalDepartureAPI (String crs) {
        this.crs = crs;
    }

    /**
     * Calls the endpoint with the crs code specified when constructing the class.
     * <p>
     * Uses the ArrDepAPIKey specified in the BuildConfig and gradle.properties to hide this
     * key from Github. The API call is to a standard REST interface, using the GET method.
     *
     * @return A JSONObject containing the parsed data returned from the API.
     */
    @Override
    public JSONObject call() {
        // Create a string builder to store the response as it's read from the input stream
        StringBuilder response = new StringBuilder();
        try {

            // Create HTTPS connection to Rail Data API
            String uri = String.format(Locale.US, "https://api1.raildata.org.uk/1010-live-arrival-and-departure-boards-arr-and-dep/LDBWS/api/20220120/GetArrDepBoardWithDetails/%s", crs);
            URL url = new URL(uri);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("x-apikey", BuildConfig.ArrDepAPIKey);
            connection.connect();

            // Check if the connection was successful
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println(connection.getResponseMessage());
                return null;
            }

            // Read JSON content from API into String
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }

        // Handle IOException that could occur during HTTPS connection
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        // Convert response object into JSON
        JSONObject json;
        try {
            json = new JSONObject(response.toString());

            // Add a timestamp the data was fetched to each service within the result
            DateFormat df = DateFormat.getTimeInstance();
            for (int i=0; i < json.getJSONArray("trainServices").length(); i++) {
                json.getJSONArray("trainServices").getJSONObject(i).put("timestamp", df.format(Calendar.getInstance().getTime()));
            }
            return json;

        // Handle any JSONExceptions that occur whilst parsing the string
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
