package com.example.meditationtimer;

import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {

    public static void saveMeditationDuration(int duration) {
        MeditationPreferences preferences = new MeditationPreferences();
        preferences.setMeditationDuration(duration);

        MyApp.database.meditationPreferencesDao().insert(preferences);
    }




    public static List<BarEntry> fetchAllSessionData() {
        List<ProgressEntry> entries = MyApp.database.progressEntryDao().getAllEntries();
        return convertToBarEntries(entries);
    }

    private static List<BarEntry> convertToBarEntries(List<ProgressEntry> entries) {
        List<BarEntry> data = new ArrayList<>();
        for (ProgressEntry entry : entries) {
            data.add(new BarEntry(entry.getTimestamp(), entry.getProgressValue()));
        }
        return data;
    }
}
