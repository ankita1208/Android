package com.example.travelplanner;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "activity_table")
public class ActivityEntity {
    public void setId(int id) {
        this.id = id;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String category;
    private String activity;
    private int imageResource;

    public ActivityEntity(String category, String activity, int imageResource) {
        this.category = category;
        this.activity = activity;
        this.imageResource = imageResource;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getActivity() {
        return activity;
    }

    public int getImageResource() {
        return imageResource;
    }
}
