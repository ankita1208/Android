package com.example.meditationtimer;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.room.Room;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

public class ProgressTracking extends AppCompatActivity {

    private BarChart sessionHistoryChart;
    private Spinner filterSpinner;

    // ProgressTrackingModel instance
    private ProgressTrackingModel progressTrackingModel;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_tracking);

        // Initialize Room Database
        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "app-database").build();

        // Find views by their IDs
        sessionHistoryChart = findViewById(R.id.sessionHistoryChart);
        filterSpinner = findViewById(R.id.filterSpinner);
        ImageView backButton = findViewById(R.id.backButton);

        // ProgressTrackingModel initialization
        progressTrackingModel = new ProgressTrackingModel(this);

        // Set up chart with sample data
        setupChart();

        // Set up spinner with actual filter options
        setupSpinner();

        // Set click listener for the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Set item selection listener for the filter spinner
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle filter selection
                String selectedFilter = (String) parentView.getItemAtPosition(position);
                Toast.makeText(ProgressTracking.this, "Selected Filter: " + selectedFilter, Toast.LENGTH_SHORT).show();
                // Implement logic to update chart data based on the selected filter
                updateChartData(selectedFilter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });
    }

    private void setupChart() {
        // Sample data for the chart
        List<BarEntry> entries = getSampleChartData();

        BarDataSet dataSet = new BarDataSet(entries, "Session History");
        BarData barData = new BarData(dataSet);

        // Customize chart appearance as needed
        sessionHistoryChart.setData(barData);
        sessionHistoryChart.invalidate(); // Refresh chart
    }

    private List<BarEntry> getSampleChartData() {
        // Sample data for the chart
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, 20));
        entries.add(new BarEntry(2, 35));
        entries.add(new BarEntry(3, 15));
        entries.add(new BarEntry(4, 40));
        return entries;
    }

    private void setupSpinner() {
        // Actual filter options
        String[] filterOptions = {"Last 7 Days", "All Sessions"};

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, filterOptions);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        filterSpinner.setAdapter(adapter);
    }

    private void updateChartData(String selectedFilter) {
        // Implement logic to update chart data based on the selected filter
        switch (selectedFilter) {
            case "Last 7 Days":
                // Fetch data for the last 7 days from Room Database
                fetchDataForLastNDays(7);
                break;


            case "All Sessions":
                // Fetch all session data from Room Database
                fetchAllSessionData();
                break;

            default:
                // Handle default case or do nothing
                break;
        }
    }

    private Handler mainHandler = new Handler(Looper.getMainLooper());

// ...

    private void fetchDataForLastNDays(int numberOfDays) {
        Executors.newSingleThreadExecutor().execute(() -> {
            long currentTime = System.currentTimeMillis();
            long durationInMillis = numberOfDays * 24 * 60 * 60 * 1000; // Convert days to milliseconds
            long startTime = currentTime - durationInMillis;

            // Fetch data for the last 30 days
            mainHandler.post(() -> {
                appDatabase.progressEntryDao().getProgressDataSinceDate(startTime).observeForever(progressEntries -> {
                    if (progressEntries != null) {
                        // The data is now available
                        Log.e("data", String.valueOf(progressEntries.size()));
                        updateChartWithData(progressEntries);
                    }
                });
            });

            // If you want to fetch data for the last 7 days as well, you can add another query
            long sevenDaysDurationInMillis = 7 * 24 * 60 * 60 * 1000; // 7 days in milliseconds
            long sevenDaysStartTime = currentTime - sevenDaysDurationInMillis;

            // Fetch data for the last 7 days
            mainHandler.post(() -> {
                appDatabase.progressEntryDao().getProgressDataSinceDate(sevenDaysStartTime).observeForever(sevenDaysProgressEntries -> {
                    if (sevenDaysProgressEntries != null) {
                        // The data for the last 7 days is now available
                        Log.e("data_7_days", String.valueOf(sevenDaysProgressEntries.size()));
                        // You can use this data as needed
                    }
                });
            });
        });
    }





    private void fetchAllSessionData() {
        // Use coroutine for database operations in a non-blocking way
        Executors.newSingleThreadExecutor().execute(() -> {
            List<ProgressEntry> data = appDatabase.progressEntryDao().getAllEntries();

            runOnUiThread(() -> {
                Log.e("data", String.valueOf(data.size()));
                updateChartWithData(data);
            });
        });
    }

    private void updateChartWithData(List<ProgressEntry> data) {
        // Convert ProgressEntry objects to BarEntry objects
        List<BarEntry> barEntries = convertToBarEntries(data);

        BarDataSet dataSet = new BarDataSet(barEntries, "Session History");
        dataSet.setColor(Color.BLUE);
        BarData barData = new BarData(dataSet);
        sessionHistoryChart.setData(barData);
        sessionHistoryChart.invalidate(); // Refresh chart
    }

    private List<BarEntry> convertToBarEntries(List<ProgressEntry> progressEntries) {
        List<BarEntry> barEntries = new ArrayList<>();
        for (ProgressEntry progressEntry : progressEntries) {
            barEntries.add(new BarEntry(progressEntry.getTimestamp(), progressEntry.getProgressValue()));
        }
        return barEntries;
    }
}