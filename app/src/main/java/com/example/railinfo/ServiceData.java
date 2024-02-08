package com.example.railinfo;

public class ServiceData {
    private final String origin;
    private final String destination;
    private String via = null;
    private String scheduled_arr;
    private String scheduled_dep = null;
    private String estimated_arr;
    private String estimated_dep = null;
    private final String operator;
    private final Integer platform;
    private Boolean cancelled = false;
    private Integer length = null;

    public ServiceData() {
        origin = "Paignton";
        destination = "Exmouth";
        via = "Exeter Central";
        scheduled_dep = "11:20";
        estimated_dep = "11:20";
        operator = "Great Western Railway";
        platform = 1;
        length = 4;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getVia() {
        return via;
    }

    public String getEstimatedArrival() {
        return estimated_arr;
    }

    public String getScheduledArrival() {
        return scheduled_arr;
    }

    public String getEstimatedDeparture() {
        return estimated_dep;
    }

    public String getScheduledDeparture() {
        return scheduled_dep;
    }

    public String getOperator() {
        return operator;
    }

    public Integer getPlatform() {
        return platform;
    }

    public Boolean isCancelled() {
        return cancelled;
    }

    public Integer getFormationLength() {
        return length;
    }
}
