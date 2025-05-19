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

    /*
    이건 수동으로 road_links 생성해주는 코드인데 현 프로젝트 상황상 자동으로 하는게 더 좋을거 같아서 주석 처리 해놓음 (추후에 쓸수도 있어서)
    @PostMapping("/auto-connect")
    public String autoConnect() {
        service.connectSequentialCenters();
        return "자동 연결 완료";
    }
    */
}
