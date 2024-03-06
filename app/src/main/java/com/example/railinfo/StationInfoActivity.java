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

public class StationInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_info);

        Intent intent = getIntent();
        String crs = intent.getStringExtra("crs");
        if (crs == null) {
            throw new RuntimeException("Missing station crs to load!");
        }

        StationData station;
        try {
            ExecutorService pool = Executors.newFixedThreadPool(3);
            Callable<StationData> callable = new StationInfoAPI(crs);
            Future<StationData> future = pool.submit(callable);
            station = future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        Toolbar toolbar = findViewById(R.id.station_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        TextView name_view = findViewById(R.id.station_name);
        TextView crs_view = findViewById(R.id.station_crs);
        TextView address_view = findViewById(R.id.station_address);

        if (station != null) {
            name_view.setText(station.getName());
            crs_view.setText(getString(R.string.brackets, station.getCrs()));
            address_view.setText(station.getAddress());

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity
        }
        return super.onOptionsItemSelected(item);
    }
}
