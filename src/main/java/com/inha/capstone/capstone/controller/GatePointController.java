package com.inha.capstone.capstone.controller;

import com.inha.capstone.capstone.apiPayload.ApiResponse;
import com.inha.capstone.capstone.apiPayload.code.status.SuccessStatus;
import com.inha.capstone.capstone.entity.GatePoint;
import com.inha.capstone.capstone.repository.GatePointRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gate-points")
@Tag(name = "GatePoint", description = "출입구(GatePoint) 관련 API")
public class GatePointController {

    private final GatePointRepository gatePointRepository;

    public GatePointController(GatePointRepository gatePointRepository) {
        this.gatePointRepository = gatePointRepository;
    }

    @GetMapping
    @Operation(
            summary = "모든 출입구 조회",
            description = "저장된 모든 출입구(GatePoint) 정보를 리스트 형태로 반환합니다."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", description = "출입구 리스트 조회 성공"
    )
    public ResponseEntity<ApiResponse<List<GatePoint>>> getAllGatePoints() {
        List<GatePoint> gates = gatePointRepository.findAll();
        return ResponseEntity.ok(ApiResponse.of(SuccessStatus._OK, gates));
    }
}