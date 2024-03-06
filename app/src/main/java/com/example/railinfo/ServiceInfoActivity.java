package com.example.railinfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.railinfo.adapters.CallingPointAdapter;
import com.example.railinfo.data.objects.ServiceData;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * An activity to display information about a given service.
 * <p>
 * Requires the data for the service to be passed through the intent, as a JSONObject,
 * formatted as a string.
 *
 * @author Sam Townley
 * @version 1.1
 */
public class ServiceInfoActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_service_info);

        // Get service data from Intent and raise runtime error if it doesn't exist
        Intent intent = getIntent();
        String data = intent.getStringExtra("service");
        if (data == null) {
            throw new RuntimeException("Missing service data to load!");
        }

        // Try to parse the data into a ServiceData object
        ServiceData service;
        try {
            service = new ServiceData(new JSONObject(data));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        // Display the content from the generated ServiceData object
        displayContent(service);

        // Setup the toolbar as an ActionBar and enable the back button
        Toolbar toolbar = findViewById(R.id.service_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
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
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Helper function designed to populate the TextFields within the layout, as well
     * as initialise the Calling Point RecyclerView.
     * <p>
     * Called once during {@link #onCreate(Bundle)}.
     *
     * @param service The ServiceData object to display data for.
     */
    private void displayContent(ServiceData service) {
        // Get views ready to add content
        TextView departure = findViewById(R.id.service_departure);
        TextView status = findViewById(R.id.service_status);
        TextView origin = findViewById(R.id.service_origin);
        TextView destination = findViewById(R.id.service_destination);
        TextView platform = findViewById(R.id.service_platform);
        TextView via = findViewById(R.id.service_via);
        ConstraintLayout inner_layout = findViewById(R.id.innner_service_layout);
        TextView time = inner_layout.findViewById(R.id.service_journey_time);
        TextView stops = inner_layout.findViewById(R.id.service_stops);
        TextView operator = inner_layout.findViewById(R.id.service_operator);
        TextView updated = findViewById(R.id.txt_last_updated);

        // Set departure/arrival time
        if (service.isDeparture()) {
            departure.setText(service.getScheduledDeparture());
        } else {
            departure.setText(service.getScheduledArrival());
        }

        // Set status of the service (On time, Expected ... or Cancelled)
        if (service.isCancelled()) {
            status.setText(getString(R.string.cancelled));
            status.setTextColor(getColor(R.color.cancelled));
        } else if (service.isDeparture() && service.isDelayedDeparture()) {
            status.setText(getString(R.string.expected, service.getEstimatedDeparture()));
            status.setTextColor(getColor(R.color.delayed));
        } else if (service.isArrival() && service.isDelayedArrival()) {
            status.setText(getString(R.string.expected, service.getEstimatedArrival()));
            status.setTextColor(getColor(R.color.delayed));
        } else {
            status.setText(getString(R.string.on_time));
            status.setTextColor(getColor(R.color.on_time));
        }

        // Set main attributes of the service
        origin.setText(service.getOriginString());
        destination.setText(service.getDestinationString());
        platform.setText(service.getPlatformString());

        // Remove via if there has one, or add it if there is
        if (service.hasVia()) {
            via.setVisibility(View.VISIBLE);
            via.setText(service.getVia());
        } else {
            via.setVisibility(View.GONE);
        }

        // Set other attributes for service
        if (service.getStops() > 0) {
            time.setText(getString(R.string.journey_time, service.getTimeToDestination()));
            stops.setText(getString(R.string.stops, service.getStops()));
        } else {
            time.setVisibility(View.GONE);
            stops.setVisibility(View.GONE);
        }
        operator.setText(getString(R.string.operator, service.getOperator()));
        updated.setText(getString(R.string.last_updated, service.getLastRefreshed()));

        // Setup calling point RecyclerView
        RecyclerView recyclerView = inner_layout.findViewById(R.id.journey_progress_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        CallingPointAdapter adapter = new CallingPointAdapter(this, service.getCallingPoints());
        recyclerView.setAdapter(adapter);
    }
}