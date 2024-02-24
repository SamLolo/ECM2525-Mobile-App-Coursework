package com.example.railinfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class ServiceData {
    private final String id;
    private final JSONArray origin;
    private final JSONArray destination;
    private final String scheduled_arr;
    private final String estimated_arr;
    private final String scheduled_dep;
    private final String estimated_dep;
    private final String operator;
    private final Boolean cancelled;
    private final Integer length;
    private final String platform;
    private JSONArray previous_calls = new JSONArray();
    private JSONArray future_calls = new JSONArray();

    public ServiceData(JSONObject data) {
        // Set service attributes from JSON object passed in
        id = data.optString("serviceID");
        origin = data.optJSONArray("origin");
        destination = data.optJSONArray("destination");
        operator = data.optString("operator");
        length = data.optInt("length");
        cancelled = data.optBoolean("isCancelled");
        platform = data.optString("platform");
        scheduled_dep = data.optString("std");
        estimated_dep = data.optString("etd");
        scheduled_arr = data.optString("sta");
        estimated_arr = data.optString("eta");

        // Try to set previous calls JSON array, catching an error if it occurs so that the attribute gets left as an empty array
        try {
            previous_calls = data.getJSONArray("previousCallingPoints").getJSONObject(0).getJSONArray("callingPoint");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Try to set future calls JSON array, catching an error if it occurs so that the attribute gets left as an empty array
        try {
            future_calls = data.getJSONArray("subsequentCallingPoints").getJSONObject(0).getJSONArray("callingPoint");
        } catch (JSONException e) {
            e.printStackTrace();
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

    public Boolean hasVia() {
        JSONObject dest = destination.optJSONObject(0);
        if (dest != null) {
            return !dest.isNull("via");
        } else {
            return false;
        }
    }

    public String getVia() {
        try {
            return destination.getJSONObject(0).optString("via");
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
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
        return (!scheduled_arr.equals("") && !estimated_arr.equals(""));
    }

    public Boolean isDeparture() {
        return (!scheduled_dep.equals("") && !estimated_dep.equals(""));
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

    public Boolean isDelayedDeparture() {
        return !estimated_dep.equals("On time");
    }

    public Boolean isDelayedArrival() {
        return !estimated_arr.equals("On time");
    }

    public String getTimeToDestination() {
        if (future_calls.length() >= 1) {
            JSONObject final_call = future_calls.optJSONObject(future_calls.length()-1);
            if (final_call != null) {
                if (!final_call.isNull("st")) {
                    String final_time = final_call.optString("st");
                    LocalTime t1 = LocalTime.parse(scheduled_dep);
                    LocalTime t2 = LocalTime.parse(final_time);
                    long minutes = ChronoUnit.MINUTES.between(t1, t2);

                    if (minutes < 0) {
                        minutes = 60*24 - minutes;
                    }

                    if (minutes >= 60) {
                        int hours = Math.toIntExact(minutes / 60);
                        if (hours == 1 && minutes == 60) {
                            return "1 hour";
                        } else if (hours == 1) {
                            return String.format(Locale.getDefault(),"1 hour, %d mins", Math.toIntExact(minutes - 60));
                        } else if (Math.toIntExact(minutes - (hours * 60L)) != 0) {
                            return String.format(Locale.getDefault(), "%d hours, %d mins", hours, Math.toIntExact(minutes - (hours * 60L)));
                        } else {
                            return String.format(Locale.getDefault(), "%d hours", hours);
                        }
                    } else {
                        return String.format(Locale.getDefault(),"%d mins", Math.toIntExact(minutes));
                    }
                }
            }
        }
        return "N/A";
    }
}
