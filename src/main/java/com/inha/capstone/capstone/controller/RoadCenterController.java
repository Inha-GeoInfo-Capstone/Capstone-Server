package com.inha.capstone.capstone.controller;

import com.inha.capstone.capstone.apiPayload.ApiResponse;
import com.inha.capstone.capstone.apiPayload.code.status.SuccessStatus;
import com.inha.capstone.capstone.entity.RoadCenter;
import com.inha.capstone.capstone.repository.RoadCenterRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/road-centers")
@Tag(name = "RoadCenter", description = "도로 중심 좌표(RoadCenter) 관련 API")
public class RoadCenterController {

    private final RoadCenterRepository repository;

    public RoadCenterController(RoadCenterRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    @Operation(
            summary = "모든 도로 중심점 조회",
            description = "저장된 모든 도로 중심 좌표(RoadCenter)를 반환합니다."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "도로 중심점 리스트 조회 성공")
    public ResponseEntity<ApiResponse<List<RoadCenter>>> getAllRoadCenters() {
        List<RoadCenter> list = repository.findAll();
        return ResponseEntity.ok(ApiResponse.of(SuccessStatus._OK, list));
    }

    /*
    @PostMapping("/bulk")
    @Operation(
            summary = "여러 도로 중심점 등록",
            description = "요청으로 받은 도로 중심 좌표들을 한 번에 저장합니다."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "도로 중심점 대량 등록 성공")
    public ResponseEntity<ApiResponse<List<RoadCenter>>> insertRoadCenters(@RequestBody List<RoadCenter> roadCenters) {
        List<RoadCenter> saved = repository.saveAll(roadCenters);
        return ResponseEntity.ok(ApiResponse.of(SuccessStatus._OK, saved));
    }
    */
}
