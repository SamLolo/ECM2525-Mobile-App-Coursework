package com.example.railinfo;

public class StationData {
    private String crs;
    private String name;
    private String address;

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
