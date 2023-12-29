package com.example.meditationtimer;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "meditation_preferences")
public class MeditationPreferences {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int meditationDuration;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMeditationDuration() {
        return meditationDuration;
    }

    public void setMeditationDuration(int meditationDuration) {
        this.meditationDuration = meditationDuration;
    }
}

