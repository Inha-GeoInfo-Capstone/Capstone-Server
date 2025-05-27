package com.inha.capstone.capstone.controller;

import com.inha.capstone.capstone.dto.GpsDataDTO;
import com.inha.capstone.capstone.entity.GatePoint;
import com.inha.capstone.capstone.entity.RoadCenter;
import com.inha.capstone.capstone.repository.GatePointRepository;
import com.inha.capstone.capstone.service.GpsService;
import com.inha.capstone.capstone.service.NavigationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/navigation")
public class NavigationController {

    private final NavigationService navigationService;
    private final GpsService gpsService; // 실시간 위치 사용
    private final GatePointRepository gatePointRepository;

    public NavigationController(NavigationService navigationService,GpsService gpsService,GatePointRepository gatePointRepository) {
        this.navigationService = navigationService;
        this.gpsService = gpsService;
        this.gatePointRepository = gatePointRepository;
    }

    @GetMapping("/shortest-path")
    public List<Map<String, Double>> getShortestPath(@RequestParam(name = "destinationId") Long destinationId) {
        GpsDataDTO current = gpsService.getLastLocation(); // 실시간 저장 위치 사용

        RoadCenter start = navigationService.findNearestCenter(current.getLatitude(), current.getLongitude());
        RoadCenter end = navigationService.getRoadCenterById(destinationId);
        List<RoadCenter> path = navigationService.findShortestPath(start, end);

        System.out.println("최단경로 출발지: " + start.getLatitude() + ", " + start.getLongitude());
        System.out.println("최단경로 도착지: " + end.getLatitude() + ", " + end.getLongitude());
        System.out.println("최단경로 결과 길이: " + path.size());

        // 프론트에서 polyline으로 경로를 그리기 위해 위도,경도 추출
        return path.stream().map(center -> {
            Map<String, Double> point = new HashMap<>();
            point.put("lat", center.getLatitude());
            point.put("lng", center.getLongitude());
            return point;
        }).toList();
    }

    @GetMapping("/current-location")
    public GpsDataDTO getCurrentLocation() {
        return gpsService.getLastLocation(); // GPS 위치 최신 데이터 반환
    }

    @GetMapping("/shortest-path-from-current")
    public List<Map<String, Double>> getPathFromCurrentToDestination(@RequestParam(name = "destinationId") Long destinationId) {
        GpsDataDTO current = gpsService.getLastLocation(); // Flask 수신 위치
        RoadCenter start = navigationService.findNearestCenter(current.getLatitude(), current.getLongitude());
        RoadCenter end = navigationService.getRoadCenterById(destinationId);

        List<RoadCenter> path = navigationService.findShortestPath(start, end);

        // 실제 GPS 좌표 추가
        List<Map<String, Double>> pathWithStart = new ArrayList<>();

        // 실시간 GPS -> 도로 중심점까지 선을 위한 마커 1개 추가
        Map<String, Double> realStart = new HashMap<>();
        realStart.put("lat", current.getLatitude());
        realStart.put("lng", current.getLongitude());
        pathWithStart.add(realStart);

        for (RoadCenter center : path) {
            Map<String, Double> point = new HashMap<>();
            point.put("lat", center.getLatitude());
            point.put("lng", center.getLongitude());
            pathWithStart.add(point);
        }

        return pathWithStart;
    }

    @GetMapping("/nearest-connection")
    public List<Map<String, Double>> getConnectionToNearestCenter() {
        GpsDataDTO current = gpsService.getLastLocation();
        RoadCenter nearest = navigationService.findNearestCenter(current.getLatitude(), current.getLongitude());

        List<Map<String, Double>> list = new ArrayList<>();

        Map<String, Double> gps = new HashMap<>();
        gps.put("lat", current.getLatitude());
        gps.put("lng", current.getLongitude());
        list.add(gps);

        Map<String, Double> center = new HashMap<>();
        center.put("lat", nearest.getLatitude());
        center.put("lng", nearest.getLongitude());
        list.add(center);

        return list;
    }

    @GetMapping("/gate-to-road-center")
    public Long convertGateToRoadCenter(@RequestParam(name = "gateId") Long gateId) {
        GatePoint gate = gatePointRepository.findById(gateId)
                .orElseThrow(() -> new IllegalArgumentException("해당 Gate ID가 존재하지 않음"));

        RoadCenter nearest = navigationService.findNearestCenter(gate.getLatitude(), gate.getLongitude());
        return nearest.getId();
    }

    @GetMapping("/nearest-center")
    public Long getNearestCenterByLatLng(@RequestParam double lat, @RequestParam double lng) {
        return navigationService.findNearestCenter(lat, lng).getId();
    }

    @GetMapping("/gate-id-by-name")
    public Long getGateIdByName(@RequestParam("name") String name) {
        GatePoint gate = gatePointRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("해당 이름의 출입구가 존재하지 않습니다."));
        return gate.getId();
    }
}