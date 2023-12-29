package com.example.travelplanner;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ActivityDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertActivity(ActivityEntity activity);

    @Query("DELETE FROM activity_table WHERE activity = :activity")
    void deleteActivity(String activity);

    @Query("SELECT * FROM activity_table")
    List<ActivityEntity> getAllActivities();
}
