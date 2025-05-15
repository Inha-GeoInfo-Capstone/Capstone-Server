package com.inha.capstone.capstone.scheduled;

import com.inha.capstone.capstone.service.GpsService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class GpsScheduler {

    private final GpsService gpsService;

    public GpsScheduler(GpsService gpsService) {
        this.gpsService = gpsService;
    }

    @Scheduled(fixedRate = 3000)
    public void fetchGpsPeriodically() {
        try {
            var data = gpsService.fetchCurrentLocation();
            System.out.println("주기적 위치 수신: " + data.getLatitude() + ", " + data.getLongitude() + " (SNR: " + data.getSnr() + ")");
        } catch (Exception e) {
            System.err.println("위치 수신 실패: " + e.getMessage());
        }
    }
}
