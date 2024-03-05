package com.example.railinfo;

public class StationData {
    private String crs;
    private String name;
    private String address;
    private float latitude;
    private float longitude;

    public StationData() {}

    public void setCrs(String crs) {
        this.crs = crs;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLatitude(float lat) {
        latitude = lat;
    }

    public void setLongitude(float lng) {
        longitude = lng;
    }

    public String getCrs() {
        return crs;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}
