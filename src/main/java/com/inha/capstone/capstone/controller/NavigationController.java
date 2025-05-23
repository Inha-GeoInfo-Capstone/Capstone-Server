package com.inha.capstone.capstone.controller;

import com.inha.capstone.capstone.dto.GpsDataDTO;
import com.inha.capstone.capstone.entity.RoadCenter;
import com.inha.capstone.capstone.service.GpsService;
import com.inha.capstone.capstone.service.NavigationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/navigation")
public class NavigationController {

    private final NavigationService navigationService;
    private final GpsService gpsService; // 실시간 위치 사용

    public NavigationController(NavigationService navigationService,GpsService gpsService) {
        this.navigationService = navigationService;
        this.gpsService = gpsService;
    }

    @GetMapping("/shortest-path")
    public List<Map<String, Double>> getShortestPath(@RequestParam Long destinationId) {
        GpsDataDTO current = gpsService.getLastLocation(); // 실시간 저장 위치 사용

        RoadCenter start = navigationService.findNearestCenter(current.getLatitude(), current.getLongitude());
        RoadCenter end = navigationService.getRoadCenterById(destinationId);
        List<RoadCenter> path = navigationService.findShortestPath(start, end);

        // 프론트에서 polyline으로 경로를 그리기 위해 위도,경도 추출
        return path.stream().map(center -> {
            Map<String, Double> point = new HashMap<>();
            point.put("lat", center.getLatitude());
            point.put("lng", center.getLongitude());
            return point;
        }).toList();
    }
}