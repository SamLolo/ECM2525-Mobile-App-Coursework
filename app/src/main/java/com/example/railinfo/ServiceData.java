package com.example.railinfo;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;

public class ServiceData {
    private final String station;
    private final String crs;
    private String id;
    private JSONArray origin;
    private JSONArray destination;
    private String scheduled_arr;
    private String estimated_arr;
    private String scheduled_dep;
    private String estimated_dep;
    private String operator;
    private Boolean cancelled;
    private Integer length;
    private String platform;
    private JSONArray previous_calls = new JSONArray();
    private JSONArray future_calls = new JSONArray();
    private Date last_refreshed = null;

    public ServiceData(JSONObject data, String station, String crs) {
        // Set service attributes from JSON object passed in
        this.station = station;
        this.crs = crs;
        loadData(data);
    }

    public ServiceData(JSONObject data) {
        // Set service attributes from JSON object passed in
        station = data.optString("locationName");
        crs = data.optString("crs");
        loadData(data);
    }

    private void loadData(JSONObject data) {
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

        DateFormat df = DateFormat.getTimeInstance();
        try {
            last_refreshed = df.parse(data.getString("timestamp"));
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }

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

    @NonNull
    @Override
    public String toString() {
        JSONObject data = new JSONObject();

        try {
            data.put("serviceID", id);
            data.put("origin", origin);
            data.put("destination", destination);
            data.put("operator", operator);
            data.put("length", length);
            data.put("isCancelled", cancelled);
            data.put("platform", platform);
            data.put("std", scheduled_dep);
            data.put("etd", estimated_dep);
            data.put("sta", scheduled_arr);
            data.put("eta", estimated_arr);
            data.put("locationName", station);
            data.put("crs", crs);
            if (last_refreshed != null) {
                DateFormat df = DateFormat.getTimeInstance();
                data.put("timestamp", df.format(last_refreshed));
            }

            JSONObject inner_array = new JSONObject().put("callingPoint", previous_calls);
            data.put("previousCallingPoints", new JSONArray().put(inner_array));

            inner_array = new JSONObject().put("callingPoint", future_calls);
            data.put("subsequentCallingPoints", new JSONArray().put(inner_array));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data.toString();
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
                        minutes = 60L*24 + minutes;
                    }

                    if (minutes >= 60) {
                        int hours = Math.toIntExact(minutes / 60);
                        if (Math.toIntExact(minutes - (hours * 60L)) != 0) {
                            return String.format(Locale.getDefault(), "%dh %dm", hours, Math.toIntExact(minutes - (hours * 60L)));
                        } else {
                            return String.format(Locale.getDefault(), "%dh", hours);
                        }
                    } else {
                        return String.format(Locale.getDefault(),"%dm", Math.toIntExact(minutes));
                    }
                }
            }
        }
        return "N/A";
    }

    public Integer getStops() {
        return future_calls.length();
    }

    public JSONArray getCallingPoints() {
        JSONArray calling_points = new JSONArray();
        for (int i=0; i < previous_calls.length(); i++) {
            calling_points.put(previous_calls.optJSONObject(i));
        }

        JSONObject current = new JSONObject();
        try {
            current.put("locationName", station);
            current.put("crs", crs);
            current.put("platform", platform);
            current.put("isCancelled", cancelled);
            if (isDeparture()) {
                current.put("st", scheduled_dep);
                current.put("et", estimated_dep);
            } else {
                current.put("st", scheduled_arr);
                current.put("et", estimated_arr);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        calling_points.put(current);

        for (int j=0; j < future_calls.length(); j++) {
            calling_points.put(future_calls.optJSONObject(j));
        }
        return calling_points;
    }

    public String getLastRefreshed() {
        if (last_refreshed != null) {
            return (String) android.text.format.DateFormat.format("hh:mm", last_refreshed);
        } else {
            return "N/A";
        }
    }

    public String getOriginCRS() {
        try {
            return origin.getJSONObject(-1).getString("crs");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getDestinationCRS() {
        try {
            return destination.getJSONObject(-1).getString("crs");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
