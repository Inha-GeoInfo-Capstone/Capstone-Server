package com.inha.capstone.capstone.controller;

import com.inha.capstone.capstone.apiPayload.ApiResponse;
import com.inha.capstone.capstone.apiPayload.code.status.SuccessStatus;
import com.inha.capstone.capstone.dto.GpsDataDTO;
import com.inha.capstone.capstone.entity.RoadCenter;
import com.inha.capstone.capstone.entity.SharedLocation;
import com.inha.capstone.capstone.service.GpsService;
import com.inha.capstone.capstone.service.NavigationService;
import com.inha.capstone.capstone.service.SharedLocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shared-location")
@Tag(name = "Shared Location", description = "실시간 위치 공유 기능 API")
public class SharedLocationController {

    private final SharedLocationService sharedLocationService;
    private final NavigationService navigationService;
    private final GpsService gpsService;

    public SharedLocationController(SharedLocationService sharedLocationService, NavigationService navigationService, GpsService gpsService) {
        this.sharedLocationService = sharedLocationService;
        this.navigationService = navigationService;
        this.gpsService = gpsService;
    }

    @PostMapping
    @Operation(summary = "위치 공유 생성", description = "현재 위치를 공유 가능한 ID로 생성합니다.")
    public ResponseEntity<ApiResponse<Long>> shareLocation(@RequestParam double lat,
                                                           @RequestParam double lng) {
        Long sharedId = sharedLocationService.createSharedLocation(lat, lng);
        return ResponseEntity.ok(ApiResponse.of(SuccessStatus._OK, sharedId));
    }

    @GetMapping("/{id}/navigate")
    @Operation(summary = "공유 위치로 길찾기", description = "공유된 위치를 바탕으로 현재 위치에서 길찾기 경로를 반환합니다.")
    public ResponseEntity<ApiResponse<List<Map<String, Double>>>> navigateToSharedLocation(@PathVariable Long id) {
        SharedLocation target = sharedLocationService.getSharedLocation(id);

        GpsDataDTO current = gpsService.getLastLocation();
        RoadCenter start = navigationService.findNearestCenter(current.getLatitude(), current.getLongitude());
        RoadCenter end = navigationService.findNearestCenter(target.getLatitude(), target.getLongitude());

        List<RoadCenter> path = navigationService.findShortestPath(start, end);
        List<Map<String, Double>> response = path.stream().map(center -> {
            Map<String, Double> p = new HashMap<>();
            p.put("lat", center.getLatitude());
            p.put("lng", center.getLongitude());
            return p;
        }).toList();

        return ResponseEntity.ok(ApiResponse.of(SuccessStatus._OK, response));
    }
}
