package com.example.meditationtimer;

import android.app.Application;

import androidx.room.Room;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyApp extends Application {

    public static AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();

        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "my_database")
                .allowMainThreadQueries() // For simplicity, allow queries on the main thread
                .build();
    }


}
