package com.technocrats.bloodlife;

public class BloodAvailabilityData {
    private String name;
    private String bloodType;
    private String bloodAvailability;

    public BloodAvailabilityData() {

    }

    public BloodAvailabilityData(String name, String bloodType, String bloodAvailability) {
        this.name = name;
        this.bloodType = bloodType;
        this.bloodAvailability = bloodAvailability;
    }

    public String getName() {
        return name;
    }

    public String getBloodType() {
        return bloodType;
    }

    public String getBloodAvailability() {
        return bloodAvailability;
    }
}
