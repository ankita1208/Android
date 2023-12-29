package com.technocrats.bloodlife;

public class BloodRequest {
    private String requesterName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String userId;
    private String requiredType;
    private String urgencyLevel;
    private String phoneNumber;
    private String email;
    private String location;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    private String bloodType;
    private String isRequester;

    public String getIsRequester() {
        return isRequester;
    }

    public void setIsRequester(String isRequester) {
        this.isRequester = isRequester;
    }

    public BloodRequest() {
        // Default constructor required for calls to DataSnapshot.getValue(BloodRequest.class)
    }

    public BloodRequest(String requesterName, String requiredType, String urgencyLevel, String phoneNumber, String email, String location, String isRequester) {
        this.requesterName = requesterName;
        this.requiredType = requiredType;
        this.urgencyLevel = urgencyLevel;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.location = location;
        this.isRequester = isRequester;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public String getRequiredType() {
        return requiredType;
    }

    public void setRequiredType(String requiredType) {
        this.requiredType = requiredType;
    }

    public String getUrgencyLevel() {
        return urgencyLevel;
    }

    public void setUrgencyLevel(String urgencyLevel) {
        this.urgencyLevel = urgencyLevel;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
