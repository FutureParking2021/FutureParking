package com.example.future_parking.classes;

import android.location.Location;

import java.util.Date;
import java.util.Map;

public class Parking {

    private ParkingId parkingId;
    private String type;
    private String name;
    private Boolean active;
    private Date createdTimestamp;
    private CreatedBy createdBy;
    private Location location;
    private Map<String, Object> itemAttributes;

    public Parking() {

    }

    public Parking(ParkingId parkingId, Location location) {
        this.parkingId = parkingId;
        this.location = location;
    }

    public Parking(ParkingId parkingId, String type, String name, Boolean active, Date createdTimestamp, CreatedBy createdBy,
                   Location location, Map<String,Object> itemAttributes) {
        super();
        this.parkingId = parkingId;
        this.type = type;
        this.name = name;
        this.active = active;
        this.createdTimestamp = createdTimestamp;
        this.createdBy = createdBy;
        this.location = location;
        this.itemAttributes = itemAttributes;
    }

    public ParkingId getParkingId() {
        return parkingId;
    }

    public Parking setParkingId(ParkingId parkingId) {
        this.parkingId = parkingId;
        return this;
    }

    public String getType() {
        return type;
    }

    public Parking setType(String type) {
        this.type = type;
        return this;
    }

    public String getName() {
        return name;
    }

    public Parking setName(String name) {
        this.name = name;
        return this;
    }

    public Boolean getActive() {
        return active;
    }

    public Parking setActive(Boolean active) {
        this.active = active;
        return this;
    }

    public Date getCreatedTimestamp() {
        return createdTimestamp;
    }

    public Parking setCreatedTimestamp(Date createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
        return this;
    }

    public CreatedBy getCreatedBy() {
        return createdBy;
    }

    public Parking setCreatedBy(CreatedBy createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public Location getLocation() {
        return location;
    }

    public Parking setLocation(Location location) {
        this.location = location;
        return this;
    }

    public Map<String, Object> getItemAttributes() {
        return itemAttributes;
    }

    public Parking setItemAttributes(Map<String, Object> itemAttributes) {
        this.itemAttributes = itemAttributes;
        return this;
    }
}
