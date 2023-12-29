package com.example.travelplanner;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// ItineraryEntity.java
@Entity(tableName = "itinerary")
public class ItineraryEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "from_location")
    private String fromLocation;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public int getAdults() {
        return adults;
    }

    public void setAdults(int adults) {
        this.adults = adults;
    }

    public int getChildren() {
        return children;
    }

    public void setChildren(int children) {
        this.children = children;
    }

    @ColumnInfo(name = "to_location")
    private String toLocation;

    @ColumnInfo(name = "departure_date")
    private String departureDate;

    @ColumnInfo(name = "return_date")
    private String returnDate;

    @ColumnInfo(name = "adults")
    private int adults;

    @ColumnInfo(name = "children")
    private int children;


}
