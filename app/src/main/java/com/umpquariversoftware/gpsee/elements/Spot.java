package com.umpquariversoftware.gpsee.elements;

import java.util.ArrayList;

/**
 * Created by robert on 3/10/17.
 */

public class Spot {

    public static final Integer LOCATION_TYPE_PAVED_ROAD = 0;
    public static final Integer LOCATION_TYPE_GRAVEL_ROAD = 1;
    public static final Integer LOCATION_TYPE_DIRT_ROAD = 2;
    public static final Integer LOCATION_TYPE_RAW_LAND = 3;
    public static final Integer LOCATION_TYPE_PAVED_TRAIL = 4;
    public static final Integer LOCATION_TYPE_UNPAVED_TRAIL = 5;
    public static final Integer LOCATION_TYPE_WATER = 6;
    public static final Integer LOCATION_TYPE_REQUIRES_SNOW_GEAR = 7;

    private String name;
    private String signature;
    private long latitude;
    private long longitude;
    private String description;
    private ArrayList<String> tags; // user builds and defines tags to allow grouping
    private ArrayList<Integer> type; // any of the enumerated location types above
    private ArrayList<String> associatedLocations; // signature of any child locations

    public Spot() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public ArrayList<Integer> getType() {
        return type;
    }

    public void setType(ArrayList<Integer> type) {
        this.type = type;
    }

    public ArrayList<String> getAssociatedLocations() {
        return associatedLocations;
    }

    public void setAssociatedLocations(ArrayList<String> associatedLocations) {
        this.associatedLocations = associatedLocations;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}

