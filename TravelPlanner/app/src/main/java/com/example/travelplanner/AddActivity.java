package com.example.travelplanner;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AddActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ActivityAdapter adapter;
    private List<ActivityItem> activityList;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        appDatabase = AppDatabase.getInstance(this);

        recyclerView = findViewById(R.id.recycleRV);
        activityList = createActivityList();
        adapter = new ActivityAdapter(activityList, appDatabase);
        recyclerView.setAdapter(adapter);
    }

    private List<ActivityItem> createActivityList() {
        List<ActivityItem> list = new ArrayList<>();
        list.add(new ActivityItem("Sightseeing", "Hiking and nature walks", R.drawable.hiking));
        list.add(new ActivityItem("Sightseeing", "Attend local festivals or events", R.drawable.fcff));
        list.add(new ActivityItem("Spa and Wellness", "Spa day or wellness retreat", R.drawable.spa));
        list.add(new ActivityItem("Adventure Sports", "Paragliding or skydiving", R.drawable.gliding));
        list.add(new ActivityItem("Photography", "Capture scenic views and landscapes", R.drawable.pg));
        list.add(new ActivityItem("Historical Sites", "Visit historical sites and monuments", R.drawable.sitess));
        list.add(new ActivityItem("Sports Events", "Attend a local sports event", R.drawable.sports));
        list.add(new ActivityItem("Wildlife Encounters", "Example Wildlife Activity", R.drawable.wildlife));
        list.add(new ActivityItem("Shopping", "Shopping spree in local markets", R.drawable.shopping));
        return list;
    }
}
