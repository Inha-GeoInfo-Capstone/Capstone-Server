package com.inha.capstone.capstone.controller;

import com.inha.capstone.capstone.entity.RoadCenter;
import com.inha.capstone.capstone.repository.RoadCenterRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/road-centers")
public class RoadCenterController {

    private final RoadCenterRepository repository;

    public RoadCenterController(RoadCenterRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<RoadCenter> getAllRoadCenters() {
        return repository.findAll();
    }


    @PostMapping("/bulk")
    public List<RoadCenter> insertRoadCenters(@RequestBody List<RoadCenter> roadCenters) {
        /*  현재는 사용하지 않지만, 확장을 위해 post 코드 작성
            프론트엔드나 Postman에서 도로 중심 좌표를 등록할 수 있도록 API 생성
            지도에서 클릭한 좌표를 중심으로 좌표를 저장할수도 있지 않을까?
        */
        return repository.saveAll(roadCenters);
    }

}
