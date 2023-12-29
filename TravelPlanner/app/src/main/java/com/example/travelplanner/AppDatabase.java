package com.example.travelplanner;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// AppDatabase.java
@Database(entities = {ItineraryEntity.class, ActivityEntity.class, MediaEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "app_database";

    private static AppDatabase instance;

    public abstract ItineraryDao itineraryDao();
    public abstract ActivityDao activityDao();
    public abstract MediaDao mediaDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    DATABASE_NAME
            ).fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}

