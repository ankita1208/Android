package com.example.travelplanner;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MediaDao {

    @Insert
    void insertMedia(MediaEntity mediaEntity);

    @Query("SELECT * FROM media")
    LiveData<List<MediaEntity>> getAllMedia();

    @Query("SELECT * FROM media WHERE id = :mediaId")
    LiveData<MediaEntity> getMediaById(int mediaId);

    @Query("UPDATE media SET title = :title, description = :description, media_url = :imageUrl WHERE id = :id")
    void updateMediaWithImage(int id, String title, String description, String imageUrl);
}

