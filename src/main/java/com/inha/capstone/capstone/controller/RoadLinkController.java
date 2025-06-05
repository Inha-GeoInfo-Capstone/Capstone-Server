package com.inha.capstone.capstone.controller;

import com.inha.capstone.capstone.apiPayload.ApiResponse;
import com.inha.capstone.capstone.apiPayload.code.status.SuccessStatus;
import com.inha.capstone.capstone.entity.RoadLink;
import com.inha.capstone.capstone.service.RoadLinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/road-links")
@Tag(name = "RoadLink", description = "도로 연결(RoadLink) 관련 API")
public class RoadLinkController {

    private final RoadLinkService service;

    public RoadLinkController(RoadLinkService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "모든 도로 연결 조회", description = "저장된 모든 도로 연결 정보를 반환합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "도로 연결 리스트 조회 성공")
    public ResponseEntity<ApiResponse<List<RoadLink>>> getAll() {
        List<RoadLink> links = service.getAllLinks();
        return ResponseEntity.ok(ApiResponse.of(SuccessStatus._OK, links));
    }

    @PostMapping
    @Operation(summary = "도로 연결 생성", description = "`fromId`와 `toId`로 도로 연결(RoadLink)을 생성합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "도로 연결 생성 성공")
    public ResponseEntity<ApiResponse<RoadLink>> createLink(@RequestParam Long fromId, @RequestParam Long toId) {
        RoadLink created = service.createLink(fromId, toId);
        return ResponseEntity.ok(ApiResponse.of(SuccessStatus._OK, created));
    }

    /*
    @PostMapping("/auto-connect")
    public ResponseEntity<ApiResponse<String>> autoConnect() {
        service.connectSequentialCenters();
        return ResponseEntity.ok(ApiResponse.of(SuccessStatus._OK, "자동 연결 완료"));
    }
    */
}