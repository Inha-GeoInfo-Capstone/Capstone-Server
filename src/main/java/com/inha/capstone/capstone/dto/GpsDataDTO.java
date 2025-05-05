package com.inha.capstone.capstone.dto;

public class GpsDataDTO {
    private double latitude;
    private double longitude;
    private int snr;

    // getters/setters
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getSnr() {
        return snr;
    }

    public void setSnr(int snr) {
        this.snr = snr;
    }
}