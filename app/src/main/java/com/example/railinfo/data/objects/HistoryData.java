package com.example.railinfo.data.objects;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * Defines the structure of an entry in the user's history of previous searches.
 * <p>
 * Holds the data associated with each entry, as well as methods to get the data so that it can
 * be displayed to the user.
 *
 * @author Sam Townley
 * @version 1.0
 */
public class HistoryData implements Comparable<HistoryData>{

    /** The name of the station that the user searched for. */
    private final String station;

    /** The Computer-Reservation System (CRS) code of the station above. */
    private final String crs;

    /** The date and time this station was last accessed. */
    private final Date date;

    /**
     * Constructs a new HistoryData object, defining the data associated with the history entry
     * at the time of construction. This data cannot be changed later.
     *
     * @param station The name of the station.
     * @param crs The CRS code of the station.
     * @param datetime The date and time the station was last accessed, formatted as a string.
     * @throws ParseException The datetime string passed in couldn't be parsed.
     */
    public HistoryData(String station, String crs, String datetime) throws ParseException {
        this.station = station;
        this.crs = crs;
        DateFormat df = DateFormat.getDateTimeInstance();
        date = df.parse(datetime);
    }

    /**
     * Compares another HistoryData object with the current object. Used automatically by
     * Collections.sort() to sort an array of HistoryData objects into descending order by the
     * date each station was last accessed.
     * <p>
     * If the integer returned is negative, then the current entry is newer than the entry it's
     * being compared to. If it returns 0, then they have equal dates and times, and if it returns
     * a positive integer, then the current entry is older than the entry it's being compared to.
     *
     * @param data the HistoryData object to be compared.
     * @return An integer defining the result of the comparison.
     */
    @Override
    public int compareTo(HistoryData data) {
        return -1*(date.compareTo(data.getDateTime()));
    }

    /**
     * Gets the name of the station associated with this entry.
     *
     * @return The name of the station.
     */
    public String getStation() {
        return station;
    }

    /**
     * Gets the CRS code that identifies the station
     *
     * @return The CRS code of the station.
     */
    public String getCrs() {
        return crs;
    }

    /**
     * Gets the time the entry was last accessed, given as a string in the format "hour:minute"
     * using the 24 hour clock.
     *
     * @return The time the entry was last accessed as a string.
     */
    public String getTime() {
        return (String) android.text.format.DateFormat.format("HH:mm", date);
    }

    /**
     * The date that the entry was last accessed, given as a string in the format
     * "day/month/year".
     *
     * @return The date that the entry was last accessed as a string.
     */
    public String getDate() {
        return (String) android.text.format.DateFormat.format("dd/MM/yyyy", date);
    }

    /**
     * Gets the date object so that it can be compared to other HistoryData objects.
     *
     * @return The {@link java.util.Date} object representing the date and time the station was last
     *         searched for.
     */
    public Date getDateTime() {
        return date;
    }
}

