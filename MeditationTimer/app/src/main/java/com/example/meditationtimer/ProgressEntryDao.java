package com.example.meditationtimer;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.Date;
import java.util.List;

@Dao
public interface ProgressEntryDao {

    @Insert
    void insert(ProgressEntry entry);

    @Query("SELECT * FROM progress_table WHERE timestamp >= :timestamp")
    LiveData<List<ProgressEntry>> getProgressDataSinceDate(long timestamp);

    @Query("SELECT * FROM progress_table")
    LiveData<List<ProgressEntry>> getAllProgressData();

    @Query("SELECT * FROM progress_table")
    List<ProgressEntry> getAllEntries();
}
