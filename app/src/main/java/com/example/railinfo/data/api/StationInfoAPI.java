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

public class StationInfoAPI implements Callable<StationData> {
    private final String crs;
    private final StationsXMLParser parser;

    public StationInfoAPI(String crs) {
        this.crs = crs;
        parser = new StationsXMLParser();
    }

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

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println(connection.getResponseMessage());
                return null;
            }

            // Read XML content from API into String
            BufferedInputStream input = new BufferedInputStream(connection.getInputStream());
            return parser.parse(input);

        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
    }
}
