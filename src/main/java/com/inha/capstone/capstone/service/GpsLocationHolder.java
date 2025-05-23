package com.inha.capstone.capstone.service;

import com.inha.capstone.capstone.dto.GpsDataDTO;
import org.springframework.stereotype.Component;

@Component
public class GpsLocationHolder {
    private double latitude;
    private double longitude;
    private double snr;

    public synchronized void update(double latitude, double longitude, double snr) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.snr = snr;
    }

    public synchronized GpsDataDTO getCurrent() {
        GpsDataDTO dto = new GpsDataDTO();
        dto.setLatitude(latitude);
        dto.setLongitude(longitude);
        dto.setSnr(snr);
        return dto;
    }
}
