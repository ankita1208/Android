package com.example.meditationtimer;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "progress_table")
public class ProgressEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int progressValue;
    private long timestamp;

    public ProgressEntry(long timestamp, int progressValue) {
        this.timestamp = timestamp;
        this.progressValue = progressValue;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProgressValue() {
        return progressValue;
    }

    public void setProgressValue(int progressValue) {
        this.progressValue = progressValue;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
