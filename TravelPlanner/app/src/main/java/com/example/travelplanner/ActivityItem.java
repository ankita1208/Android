package com.example.travelplanner;

public class ActivityItem {
    private String category;
    private String activity;
    private int imageResource;

    public ActivityItem(String category, String activity, int imageResource) {
        this.category = category;
        this.activity = activity;
        this.imageResource = imageResource;
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

