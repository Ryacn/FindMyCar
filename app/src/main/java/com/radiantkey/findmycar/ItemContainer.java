package com.radiantkey.findmycar;

public class ItemContainer {
    private long id;
    private String name;
    private String note;
    private long time;
    private double lat;
    private double lng;
    private double alt;

    public ItemContainer(long id, String name, String cat, long startTime, double lat, double lng, double alt) {
        this.id = id;
        this.name = name;
        this.note = cat;
        this.time = startTime;
        this.lat = lat;
        this.lng = lng;
        this.alt = alt;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public long getTime() {
        return time;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public double getAlt() {
        return alt;
    }

    public void setAlt(double alt) {
        this.alt = alt;
    }
}
