package com.example.railinfo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

public class ServiceInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_info);

        Intent intent = getIntent();
        String data = intent.getStringExtra("service");

        ServiceData service = null;
        try {
            service = new ServiceData(new JSONObject(data));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        System.out.println(service.getDestinationString());
    }
}