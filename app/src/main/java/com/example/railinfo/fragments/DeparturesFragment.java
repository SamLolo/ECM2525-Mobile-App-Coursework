package com.example.railinfo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.railinfo.R;
import com.example.railinfo.data.objects.ServiceData;
import com.example.railinfo.StationBoardActivity;
import com.example.railinfo.adapters.DepartureAdapter;

import java.util.ArrayList;

/**
 * Fragment to show the departures for a service within a RecyclerView.
 *
 * @author Sam Townley
 * @version 1.0
 */
public class DeparturesFragment extends Fragment {

    /**
     * Inflates the layout when the fragment is first created.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     *                 any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's
     *                  UI should be attached to.  The fragment should not add the view itself,
     *                  but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     *
     * @return The root View of the inflated layout for the fragment.
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.station_board_recycler_view, container, false);
    }

    /**
     * Called immediately after creating the view.
     * <p>
     * Creates the RecyclerView layout manager and creates the adapter for departures after
     * filtering the services data returned from {@link StationBoardActivity}.
     *
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        // Set layout manager of RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.departures_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        // Get services and filter only departing services
        ArrayList<ServiceData> services = StationBoardActivity.getServices();
        ArrayList<ServiceData> departures = new ArrayList<>();
        for (int i=0; i < services.size(); i++) {
            ServiceData service = services.get(i);
            if (service.isDeparture()) {
                departures.add(service);
            }
        }

        // Create the adapter and bind it to the RecyclerView.
        DepartureAdapter adapter = new DepartureAdapter(this.getContext(), departures);
        recyclerView.setAdapter(adapter);
    }
}
