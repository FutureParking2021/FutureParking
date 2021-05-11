package com.example.future_parking.classes;

public class ParkingId {

    private String space;
    private String id;

    public ParkingId() {
    }

    public ParkingId(String space, String id) {
        this.space = space;
        this.id = id;
    }

    public String getSpace() {
        return space;
    }

    public ParkingId setSpace(String space) {
        this.space = space;
        return this;
    }

    public String getId() {
        return id;
    }

    public ParkingId setId(String id) {
        this.id = id;
        return this;
    }
}
