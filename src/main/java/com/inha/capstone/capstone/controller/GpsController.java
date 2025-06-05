package com.inha.capstone.capstone.controller;

import com.inha.capstone.capstone.apiPayload.ApiResponse;
import com.inha.capstone.capstone.apiPayload.code.status.SuccessStatus;
import com.inha.capstone.capstone.dto.GpsDataDTO;
import com.inha.capstone.capstone.service.GpsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gps")
@Tag(name = "GPS", description = "GPS 관련 API")
public class GpsController {

    private final GpsService gpsService;

    public GpsController(GpsService gpsService) {
        this.gpsService = gpsService;
    }

    @GetMapping
    @Operation(
            summary = "현재 위치 조회",
            description = "실시간 GPS 위치 정보를 반환합니다."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "현재 위치 조회 성공")
    public ResponseEntity<ApiResponse<GpsDataDTO>> getCurrentLocation() {
        GpsDataDTO location = gpsService.fetchCurrentLocation();
        return ResponseEntity.ok(ApiResponse.of(SuccessStatus._OK, location));
    }
}