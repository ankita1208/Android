package com.example.meditationtimer;

import android.content.Context;
import androidx.lifecycle.LiveData;

import com.example.meditationtimer.AppDatabase;
import com.example.meditationtimer.MyApp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ProgressTrackingModel {

    private ProgressEntryDao progressDao;
    private LiveData<List<ProgressEntry>> allProgressData;

    public ProgressTrackingModel(Context context) {
        // Use the application context to avoid memory leaks
        Context appContext = context.getApplicationContext();

        // Assuming you have a MyApp class with a static database field
        AppDatabase database = MyApp.database;

        progressDao = database.progressEntryDao();
        allProgressData = progressDao.getAllProgressData();
    }

    public LiveData<List<ProgressEntry>> getAllProgressData() {
        return allProgressData;
    }

//    public LiveData<List<ProgressEntry>> getProgressDataLastNDays(int numberOfDays) {
//        // Calculate the date N days ago
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Date());
//        calendar.add(Calendar.DAY_OF_YEAR, -numberOfDays);
//        Date startDate = calendar.getTime();
//
//        // Use the DAO to get progress data for the last N days
//        return progressDao.getProgressDataSinceDate(startDate);
//    }

    public void insertProgressData(ProgressEntry progressEntity) {
        // Use the DAO to insert progress data into the database
        AppDatabase.databaseWriteExecutor.execute(() -> {
            progressDao.insert(progressEntity);
        });
    }
}
