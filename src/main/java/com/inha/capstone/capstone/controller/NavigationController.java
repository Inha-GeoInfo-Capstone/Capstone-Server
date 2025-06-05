package com.inha.capstone.capstone.controller;

import com.inha.capstone.capstone.apiPayload.ApiResponse;
import com.inha.capstone.capstone.apiPayload.code.status.SuccessStatus;
import com.inha.capstone.capstone.dto.GpsDataDTO;
import com.inha.capstone.capstone.entity.GatePoint;
import com.inha.capstone.capstone.entity.RoadCenter;
import com.inha.capstone.capstone.repository.GatePointRepository;
import com.inha.capstone.capstone.service.GpsService;
import com.inha.capstone.capstone.service.NavigationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
@RequestMapping("/api/navigation")
@Tag(name = "Navigation", description = "네비게이션 경로 관련 API")
public class NavigationController {

    private final NavigationService navigationService;
    private final GpsService gpsService;
    private final GatePointRepository gatePointRepository;

    public NavigationController(NavigationService navigationService, GpsService gpsService, GatePointRepository gatePointRepository) {
        this.navigationService = navigationService;
        this.gpsService = gpsService;
        this.gatePointRepository = gatePointRepository;
    }

    @GetMapping("/shortest-path")
    @Operation(summary = "최단 경로 조회", description = "실시간 위치에서 목적지까지 최단 경로를 반환합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "최단 경로 조회 성공")
    public ResponseEntity<ApiResponse<List<Map<String, Double>>>> getShortestPath(@RequestParam Long destinationId) {
        GpsDataDTO current = gpsService.getLastLocation();
        RoadCenter start = navigationService.findNearestCenter(current.getLatitude(), current.getLongitude());
        RoadCenter end = navigationService.getRoadCenterById(destinationId);
        List<RoadCenter> path = navigationService.findShortestPath(start, end);

        List<Map<String, Double>> response = pathToLatLngList(path);
        return ResponseEntity.ok(ApiResponse.of(SuccessStatus._OK, response));
    }

    @GetMapping("/current-location")
    @Operation(summary = "현재 GPS 위치 조회", description = "Flask에서 가져온 최신 GPS 위치를 반환합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "현재 GPS 위치 조회 성공")
    public ResponseEntity<ApiResponse<GpsDataDTO>> getCurrentLocation() {
        return ResponseEntity.ok(ApiResponse.of(SuccessStatus._OK, gpsService.getLastLocation()));
    }

    @GetMapping("/shortest-path-from-current")
    @Operation(summary = "실제 GPS부터 최단 경로", description = "실제 GPS 위치에서 도로 경로 시작점까지 포함한 최단 경로를 반환합니다.")
    public ResponseEntity<ApiResponse<List<Map<String, Double>>>> getPathFromCurrentToDestination(@RequestParam Long destinationId) {
        GpsDataDTO current = gpsService.getLastLocation();
        RoadCenter start = navigationService.findNearestCenter(current.getLatitude(), current.getLongitude());
        RoadCenter end = navigationService.getRoadCenterById(destinationId);
        List<RoadCenter> path = navigationService.findShortestPath(start, end);

        List<Map<String, Double>> pathWithStart = new ArrayList<>();
        pathWithStart.add(latLngMap(current.getLatitude(), current.getLongitude()));
        pathWithStart.addAll(pathToLatLngList(path));

        return ResponseEntity.ok(ApiResponse.of(SuccessStatus._OK, pathWithStart));
    }

    @GetMapping("/nearest-connection")
    @Operation(summary = "실제 위치 - 도로 중심점 연결", description = "실제 위치와 연결되는 가장 가까운 도로 중심점 2개를 반환합니다.")
    public ResponseEntity<ApiResponse<List<Map<String, Double>>>> getConnectionToNearestCenter() {
        GpsDataDTO current = gpsService.getLastLocation();
        RoadCenter nearest = navigationService.findNearestCenter(current.getLatitude(), current.getLongitude());

        List<Map<String, Double>> connection = Arrays.asList(
                latLngMap(current.getLatitude(), current.getLongitude()),
                latLngMap(nearest.getLatitude(), nearest.getLongitude())
        );

        return ResponseEntity.ok(ApiResponse.of(SuccessStatus._OK, connection));
    }

    @GetMapping("/gate-to-road-center")
    @Operation(summary = "출입구 -> 도로 중심점 ID 변환", description = "특정 출입구의 위치에서 가장 가까운 도로 중심점의 ID를 반환합니다.")
    public ResponseEntity<ApiResponse<Long>> convertGateToRoadCenter(@RequestParam Long gateId) {
        GatePoint gate = gatePointRepository.findById(gateId)
                .orElseThrow(() -> new IllegalArgumentException("해당 Gate ID가 존재하지 않음"));
        RoadCenter nearest = navigationService.findNearestCenter(gate.getLatitude(), gate.getLongitude());
        return ResponseEntity.ok(ApiResponse.of(SuccessStatus._OK, nearest.getId()));
    }

    @GetMapping("/nearest-center")
    @Operation(summary = "위치 기준 도로 중심점 찾기", description = "위도, 경도를 기준으로 가장 가까운 도로 중심점 ID를 반환합니다.")
    public ResponseEntity<ApiResponse<Long>> getNearestCenterByLatLng(@RequestParam double lat, @RequestParam double lng) {
        return ResponseEntity.ok(ApiResponse.of(SuccessStatus._OK, navigationService.findNearestCenter(lat, lng).getId()));
    }

    @GetMapping("/gate-id-by-name")
    @Operation(summary = "출입구 이름 -> ID", description = "출입구 이름으로 해당 출입구의 ID를 반환합니다.")
    public ResponseEntity<ApiResponse<Long>> getGateIdByName(@RequestParam String name) {
        GatePoint gate = gatePointRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("해당 이름의 출입구가 존재하지 않습니다."));
        return ResponseEntity.ok(ApiResponse.of(SuccessStatus._OK, gate.getId()));
    }

    @GetMapping("/gate-name-suggestions")
    @Operation(summary = "출입구 이름 자동완성", description = "이름 일부로 시작하는 출입구 이름 리스트를 반환합니다.")
    public ResponseEntity<ApiResponse<List<String>>> suggestGateNames(@RequestParam String query) {
        List<String> names = gatePointRepository.findByNameContaining(query)
                .stream().map(GatePoint::getName).toList();
        return ResponseEntity.ok(ApiResponse.of(SuccessStatus._OK, names));
    }

    @GetMapping("/destination-to-nearest-gate")
    @Operation(summary = "도로 중심점 -> 가장 가까운 출입구", description = "목적지 좌표에서 가장 가까운 출입구를 반환합니다.")
    public ResponseEntity<ApiResponse<Map<String, Double>>> getNearestGateToDestination(@RequestParam Long centerId) {
        RoadCenter destination = navigationService.getRoadCenterById(centerId);
        List<GatePoint> allGates = gatePointRepository.findAll();

        GatePoint nearestGate = null;
        double minDistance = Double.MAX_VALUE;

        for (GatePoint gate : allGates) {
            double dist = navigationService.calculateDistance(
                    destination.getLatitude(), destination.getLongitude(),
                    gate.getLatitude(), gate.getLongitude());

            if (dist < minDistance) {
                minDistance = dist;
                nearestGate = gate;
            }
        }

        Map<String, Double> result = latLngMap(nearestGate.getLatitude(), nearestGate.getLongitude());
        return ResponseEntity.ok(ApiResponse.of(SuccessStatus._OK, result));
    }

    // 위도 경도 리스트를 함수로 분리
    private Map<String, Double> latLngMap(double lat, double lng) {
        Map<String, Double> map = new HashMap<>();
        map.put("lat", lat);
        map.put("lng", lng);
        return map;
    }

    // 위도,경도 반환 함수
    private List<Map<String, Double>> pathToLatLngList(List<RoadCenter> path) {
        return path.stream()
                .map(c -> latLngMap(c.getLatitude(), c.getLongitude()))
                .toList();
    }
}