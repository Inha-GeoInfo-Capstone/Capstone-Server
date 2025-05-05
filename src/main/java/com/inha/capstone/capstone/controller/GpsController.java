package com.inha.capstone.capstone.controller;

import com.inha.capstone.capstone.dto.GpsDataDTO;
import com.inha.capstone.capstone.service.GpsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gps")
public class GpsController {

    private final GpsService gpsService;

    public GpsController(GpsService gpsService) {
        this.gpsService = gpsService;
    }

    @GetMapping
    public GpsDataDTO getCurrentLocation() {
        return gpsService.fetchCurrentLocation();
    }
}