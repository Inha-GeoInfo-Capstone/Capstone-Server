package com.inha.capstone.capstone.controller;

import com.inha.capstone.capstone.entity.RoadLink;
import com.inha.capstone.capstone.service.RoadLinkService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/road-links")
public class RoadLinkController {

    private final RoadLinkService service;

    public RoadLinkController(RoadLinkService service) {
        this.service = service;
    }

    @GetMapping
    public List<RoadLink> getAll() {
        return service.getAllLinks();
    }

    @PostMapping
    public RoadLink createLink(@RequestParam Long fromId, @RequestParam Long toId) {
        return service.createLink(fromId, toId);
    }

    @PostMapping("/auto-connect")
    public String autoConnect() {
        service.connectSequentialCenters();
        return "자동 연결 완료";
    }
}
