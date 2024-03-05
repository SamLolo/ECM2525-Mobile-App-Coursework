package com.example.railinfo.data.objects;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class HistoryData implements Comparable<HistoryData>{
    private final String station;
    private final String crs;
    private final Date date;

    public HistoryData(String station, String crs, String datetime) throws ParseException {
        this.station = station;
        this.crs = crs;
        DateFormat df = DateFormat.getDateTimeInstance();
        date = df.parse(datetime);
    }

    @Override
    public int compareTo(HistoryData data) {
        return -1*(date.compareTo(data.getDateTime()));
    }

    public String getStation() {
        return station;
    }

    public String getCrs() {
        return crs;
    }

    public String getTime() {
        return (String) android.text.format.DateFormat.format("HH:mm", date);
    }

    public String getDate() {
        return (String) android.text.format.DateFormat.format("dd/MM/yyyy", date);
    }

    public Date getDateTime() {
        return date;
    }
}

