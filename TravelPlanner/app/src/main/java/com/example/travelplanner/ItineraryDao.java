package com.example.travelplanner;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ItineraryDao {

    @Insert
    void insert(ItineraryEntity itinerary);

    @Query("SELECT * FROM itinerary")
    LiveData<List<ItineraryEntity>> getAllItineraries();
}
