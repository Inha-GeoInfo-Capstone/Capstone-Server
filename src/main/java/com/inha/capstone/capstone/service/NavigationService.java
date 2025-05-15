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

    // 도로 연결 정보를 그래프 형식으로 구성
    public Map<RoadCenter, List<RoadLink>> buildGraph() {
        Map<RoadCenter, List<RoadLink>> graph = new HashMap<>();
        List<RoadLink> links = roadLinkRepository.findAll();

        for (RoadLink link : links) {
            graph.computeIfAbsent(link.getFrom(), k -> new ArrayList<>()).add(link);
        }
        return graph;
    }

    // 사용자 위치에서 가장 가까운 도로 중심을 찾는 함수 구현
    public RoadCenter findNearestCenter(double userLat, double userLon) {
        List<RoadCenter> centers = roadCenterRepository.findAll();
        RoadCenter nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (RoadCenter center : centers) {
            double dist = haversine(userLat, userLon, center.getLatitude(), center.getLongitude());
            if (dist < minDistance) {
                minDistance = dist;
                nearest = center;
            }
        }
        return nearest;
    }

    // 위도,경도 좌표 사이의 거리 계산
    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // 지구 반지름 (km)
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
