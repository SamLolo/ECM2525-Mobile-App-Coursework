package com.example.railinfo.data.api;

import com.example.railinfo.BuildConfig;
import com.example.railinfo.data.objects.StationData;
import com.example.railinfo.data.xml.StationsXMLParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.concurrent.Callable;

import javax.net.ssl.HttpsURLConnection;

/**
 * A callable task for fetching station information from the National Rail API.
 * Designed as callable so it can be executed off the main thread by an activity.
 * <p>
 * Uses the Knowledgebase Stations data feed endpoint from the National Rail's Rail Data
 * Marketplace. This endpoint returns information (in XML) about the station specified
 * using the CRS code. For more information, visit
 * <a href="https://raildata.org.uk/dataProduct/P-88ffe920-471c-4fd9-8e0d-95d5b9b7a257/overview">https://raildata.org.uk/</a>
 *
 * @author Sam Townley
 * @version 1.0
 */
public class StationInfoAPI implements Callable<StationData> {

    /** The Computer-Reservation System (CRS) code of the station to get information for. */
    private final String crs;

    /** The instance of the XMLParser to use when reading the content returned from the API */
    private final StationsXMLParser parser;

    /**
     * Constructs a new callable API task for retrieving information about a specified station.
     *
     * @param crs The CRS code of the station to fetch information about.
     */
    public StationInfoAPI(String crs) {
        this.crs = crs;
        parser = new StationsXMLParser();
    }

    /**
     * Calls the endpoint with the crs code specified when constructing the class.
     * <p>
     * Uses the StationsAPIKey specified in the BuildConfig and gradle.properties to hide this
     * key from Github. The API call is to a standard REST interface, using the GET method.
     *
     * @return A JSONObject containing the parsed data returned from the API.
     */
    @Override
    public StationData call() {
        try {
            // Create HTTPS connection to Rail Data API
            String uri = String.format(Locale.US, "https://api1.raildata.org.uk/1010-knowlegebase-stations-xml-feed1_1/4.0/station-%s.xml", crs);
            URL url = new URL(uri);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("x-apikey", BuildConfig.StationsAPIKey);
            connection.connect();

            // Check if the connection was successful
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println(connection.getResponseMessage());
                return null;
            }

            // Get a BufferedInputStream and pass this to the parser to read, returning the
            // resulting StationData object.
            BufferedInputStream input = new BufferedInputStream(connection.getInputStream());
            return parser.parse(input);

        // Handle any IOExceptions that occur whilst carrying out the HTTPS request and
        // handle any XmlPullParserExceptions that occur whilst parsing the XML.
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
    }
}
