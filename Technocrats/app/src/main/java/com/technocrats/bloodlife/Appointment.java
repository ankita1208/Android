package com.technocrats.bloodlife;

import java.util.Date;

public class Appointment {
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDonorId() {
        return donorId;
    }

    public void setDonorId(String donorId) {
        this.donorId = donorId;
    }

    public Date getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Date appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    private String donorId;
    private Date appointmentTime;

    public Appointment() {
    }

    public Appointment(String userId, String donorId, long timestamp) {
        this.userId = userId;
        this.donorId = donorId;
        this.appointmentTime = new Date(timestamp);
    }


}
