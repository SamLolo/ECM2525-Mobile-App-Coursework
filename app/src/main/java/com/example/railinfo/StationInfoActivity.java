package com.example.railinfo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.railinfo.data.api.StationInfoAPI;
import com.example.railinfo.data.objects.StationData;

import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * An activity to display information about a specified station.
 * <p>
 * Requires a station CRS code to passed in to load correctly.
 *
 * @author Sam Townley
 * @version 1.0
 */
public class StationInfoActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_station_info);

        // Get CRS code from intent
        Intent intent = getIntent();
        String crs = intent.getStringExtra("crs");
        if (crs == null) {
            throw new RuntimeException("Missing station crs to load!");
        }

        // Get station data from National Rail API, using an ExecutorService off the main thread
        StationData station;
        try {
            ExecutorService pool = Executors.newFixedThreadPool(3);
            Callable<StationData> callable = new StationInfoAPI(crs);
            Future<StationData> future = pool.submit(callable);
            station = future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Setup the toolbar as an ActionBar and enable the back button
        Toolbar toolbar = findViewById(R.id.station_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Get the views that need to be populated
        TextView name_view = findViewById(R.id.station_name);
        TextView crs_view = findViewById(R.id.station_crs);
        TextView address_view = findViewById(R.id.station_address);

        // If a station was loaded correctly, add the data to the TextViews
        if (station != null) {
            name_view.setText(station.getName());
            crs_view.setText(getString(R.string.brackets, station.getCrs()));
            address_view.setText(station.getAddress());

            // Add an implicit intent to the button onClick() which open google maps and gets directions.
            Button btn_directions = findViewById(R.id.btn_directions);
            btn_directions.setOnClickListener(v -> {
                String mapsUrl = String.format(Locale.getDefault(), "google.navigation:q=%s", station.getAddress().replaceAll("\\s", "+"));
                Intent mapsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mapsUrl));
                if (mapsIntent.resolveActivity(getPackageManager()) != null){
                    startActivity(mapsIntent);
                }
            });
        }
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
        // If the back arrow is clicked, finish up the activity and return the previous activity
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
