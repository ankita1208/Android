package com.example.meditationtimer;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MeditationPreferencesDao {

    @Insert
    void insert(MeditationPreferences preferences);

    @Query("SELECT * FROM meditation_preferences LIMIT 1")
    MeditationPreferences getMeditationPreferences();
}
