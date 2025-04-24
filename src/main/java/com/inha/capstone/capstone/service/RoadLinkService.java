package com.inha.capstone.capstone.service;

import com.inha.capstone.capstone.entity.RoadCenter;
import com.inha.capstone.capstone.entity.RoadLink;
import com.inha.capstone.capstone.repository.RoadCenterRepository;
import com.inha.capstone.capstone.repository.RoadLinkRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoadLinkService {

    private final RoadLinkRepository roadLinkRepository;
    private final RoadCenterRepository roadCenterRepository;

    public RoadLinkService(RoadLinkRepository roadLinkRepository, RoadCenterRepository roadCenterRepository) {
        this.roadLinkRepository = roadLinkRepository;
        this.roadCenterRepository = roadCenterRepository;
    }

    public List<RoadLink> getAllLinks() {
        return roadLinkRepository.findAll();
    }

    public RoadLink createLink(Long fromId, Long toId) {
        RoadCenter from = roadCenterRepository.findById(fromId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 도로 중심점입니다."));
        RoadCenter to = roadCenterRepository.findById(toId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 to_center_id 값입니다."));

        return roadLinkRepository.save(new RoadLink(from, to));
    }
}
