package com.example.travelplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TripAdapter tripAdapter;
    private AppDatabase appDatabase;

    private Button planBT, galleryBT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appDatabase = AppDatabase.getInstance(this);

        planBT = findViewById(R.id.planBT);
        galleryBT = findViewById(R.id.galleryBT);
        recyclerView = findViewById(R.id.recycleRV);

        planBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ItinaryPlanner.class);
                startActivity(intent);
            }
        });

        planBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ItinaryPlanner.class);
                startActivity(intent);
            }
        });

        galleryBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AccessGallery.class);
                startActivity(intent);
            }
        });

        loadTrips();
    }

    private void loadTrips() {
        new LoadTripsAsyncTask().execute();
    }

    private class LoadTripsAsyncTask extends AsyncTask<Void, Void, LiveData<List<ItineraryEntity>>> {
        @Override
        protected LiveData<List<ItineraryEntity>> doInBackground(Void... voids) {
            return appDatabase.itineraryDao().getAllItineraries();
        }

        @Override
        protected void onPostExecute(LiveData<List<ItineraryEntity>> liveData) {
            tripAdapter = new TripAdapter(new ArrayList<>());
            recyclerView.setAdapter(tripAdapter);
            liveData.observe(MainActivity.this, new Observer<List<ItineraryEntity>>() {
                @Override
                public void onChanged(List<ItineraryEntity> itineraries) {
                    tripAdapter.setItineraries(itineraries);
                }
            });
        }
    }
}
