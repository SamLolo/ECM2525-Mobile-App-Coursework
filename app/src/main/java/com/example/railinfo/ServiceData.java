package com.example.railinfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ServiceData {
    private final String id;
    private final JSONArray origin;
    private final JSONArray destination;
    private final String scheduled_arr;
    private final String estimated_arr;
    private Boolean arrival = false;
    private final String scheduled_dep;
    private final String estimated_dep;
    private Boolean departure = false;
    private final String operator;
    private final Boolean cancelled;
    private final Integer length;
    private final String platform;

    public ServiceData(JSONObject data) {
        id = data.optString("serviceID");
        origin = data.optJSONArray("origin");
        destination = data.optJSONArray("destination");
        operator = data.optString("operator");
        length = data.optInt("length");
        cancelled = data.optBoolean("isCancelled");
        platform = data.optString("platform");

        scheduled_dep = data.optString("std");
        estimated_dep = data.optString("etd");
        if (!scheduled_dep.equals("") && !estimated_dep.equals("")) {
            departure = true;
        }

        scheduled_arr = data.optString("sta");
        estimated_arr = data.optString("eta");
        if (!scheduled_arr.equals("") && !estimated_arr.equals("")) {
            arrival = true;
        }
    }

    public String getServiceID() {
        return id;
    }

    public String getOriginString() {
        StringBuilder sb = new StringBuilder();
        int i;
        for (i = 0; i < origin.length(); i++) {
            try {
                JSONObject location = origin.getJSONObject(i);
                if (origin.length()-i > 1 && i > 0) {
                    sb.append(", ");
                } else if (i > 0) {
                    sb.append(" &amp; ");
                }
                sb.append(location.optString("locationName"));
                sb.append(" (").append(location.optString("crs")).append(")");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public String getDestinationString() {
        StringBuilder sb = new StringBuilder();
        int i;
        for (i = 0; i < destination.length(); i++) {
            try {
                JSONObject location = destination.getJSONObject(i);
                if (destination.length()-i > 1 && i > 0) {
                    sb.append(", ");
                } else if (i > 0) {
                    sb.append(" &amp; ");
                }
                sb.append(location.optString("locationName"));
                sb.append(" (").append(location.optString("crs")).append(")");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public Boolean isArrival() {
        return arrival;
    }

    public Boolean isDeparture() {
        return departure;
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

    public String getPlatformString() {
        if (!platform.equals("")) {
            return platform;
        } else {
            return "N/A";
        }
    }

    public Boolean isCancelled() {
        return cancelled;
    }

    public Boolean hasFormationLength() {
        return !length.equals(0);
    }

    public Integer getFormationLength() {
        return length;
    }
}
