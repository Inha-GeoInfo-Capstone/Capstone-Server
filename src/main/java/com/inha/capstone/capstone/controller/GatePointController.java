package com.inha.capstone.capstone.controller;

import com.inha.capstone.capstone.entity.GatePoint;
import com.inha.capstone.capstone.repository.GatePointRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gate-points")
public class GatePointController {

    private final GatePointRepository gatePointRepository;

    public GatePointController(GatePointRepository gatePointRepository) {
        this.gatePointRepository = gatePointRepository;
    }

    @GetMapping
    public List<GatePoint> getAllGatePoints() {
        return gatePointRepository.findAll();
    }
}