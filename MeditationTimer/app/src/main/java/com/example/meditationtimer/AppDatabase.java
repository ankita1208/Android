package com.example.meditationtimer;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {MeditationPreferences.class, ProgressEntry.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(4);
    private static volatile AppDatabase INSTANCE;
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "app-database"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract MeditationPreferencesDao meditationPreferencesDao();

    public abstract ProgressEntryDao progressEntryDao();
}

