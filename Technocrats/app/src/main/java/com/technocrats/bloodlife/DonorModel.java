package com.technocrats.bloodlife;

import com.google.firebase.firestore.PropertyName;

public class DonorModel {
    @PropertyName("userId")
    private String userId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @PropertyName("name")
    private String name;

    @PropertyName("isBloodDonor")
    private boolean isBloodDonor;

    public long getAvailabilityTime() {
        return availabilityTime;
    }

    public void setAvailabilityTime(long availabilityTime) {
        this.availabilityTime = availabilityTime;
    }

    @PropertyName("availabilityTime")
    private long availabilityTime;


    public DonorModel() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isBloodDonor() {
        return isBloodDonor;
    }

    public void setBloodDonor(boolean bloodDonor) {
        isBloodDonor = bloodDonor;
    }

}

