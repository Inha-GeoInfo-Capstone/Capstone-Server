package com.inha.capstone.capstone.service;

import com.inha.capstone.capstone.entity.RoadCenter;
import com.inha.capstone.capstone.entity.RoadLink;
import com.inha.capstone.capstone.repository.RoadCenterRepository;
import com.inha.capstone.capstone.repository.RoadLinkRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NavigationService {

    private final RoadCenterRepository roadCenterRepository;
    private final RoadLinkRepository roadLinkRepository;

    public NavigationService(RoadCenterRepository roadCenterRepository,
                             RoadLinkRepository roadLinkRepository) {
        this.roadCenterRepository = roadCenterRepository;
        this.roadLinkRepository = roadLinkRepository;
    }

    public Map<RoadCenter, List<RoadLink>> buildGraph() {
        Map<RoadCenter, List<RoadLink>> graph = new HashMap<>();
        List<RoadLink> links = roadLinkRepository.findAll();

        for (RoadLink link : links) {
            graph.computeIfAbsent(link.getFrom(), k -> new ArrayList<>()).add(link);
        }
        return graph;
    }
}
