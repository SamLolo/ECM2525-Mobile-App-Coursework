package com.example.railinfo.data.objects;

/**
 * Defines the structure of a station that's returned from the National Rail API
 * <p>
 * Holds the necessary data associated with each station, and has methods to get and set the
 * data as it's fetched from the API.
 *
 * @author Sam Townley
 * @version 1.0
 */
public class StationData {

    /** The Computer-Reservation System (CRS) code that identifies this station. */
    private String crs;

    /** The name of the station. */
    private String name;

    /** The full address string of the station. */
    private String address;

    /**
     * Constructs a new empty StationData object that can then be filled with data using the
     * appropriate setter methods.
     */
    public StationData() {}

    /**
     * Sets the CRS code of the station.
     *
     * @param crs The CRS code that identifies the station.
     */
    public void setCrs(String crs) {
        this.crs = crs;
    }

    /**
     * Sets the name of the station.
     *
     * @param name The name of the station.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the address of the station.
     *
     * @param address The full address string of the station.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the CRS code of the station.
     *
     * @return The CRS code of the station.
     */
    public String getCrs() {
        return crs;
    }

    /**
     * Gets the name of the station.
     *
     * @return The name of the station.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the address of the station.
     *
     * @return The full address string of the station.
     */
    public String getAddress() {
        return address;
    }
}