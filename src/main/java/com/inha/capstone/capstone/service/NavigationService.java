package com.inha.capstone.capstone.service;

import com.inha.capstone.capstone.entity.RoadCenter;
import com.inha.capstone.capstone.entity.RoadLink;
import com.inha.capstone.capstone.repository.RoadCenterRepository;
import com.inha.capstone.capstone.repository.RoadLinkRepository;
import org.springframework.stereotype.Service;

import java.util.*;

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

    // 최단 경로 탐색을 위한 다익스트라 알고리즘 구현
    public List<RoadCenter> findShortestPath(RoadCenter start, RoadCenter end) {
        Map<RoadCenter, List<RoadLink>> graph = buildGraph();

        // 각 노드까지의 최단 거리 기록 맵
        Map<RoadCenter, Double> distance = new HashMap<>();
        // 경로 추적용 맵
        Map<RoadCenter, RoadCenter> previous = new HashMap<>();
        // 최단 거리 기준으로 탐색
        PriorityQueue<RoadCenter> pq = new PriorityQueue<>(Comparator.comparingDouble(distance::get));

        for (RoadCenter node : graph.keySet()) {
            distance.put(node, Double.MAX_VALUE); // 노드 거리 초기화
        }
        distance.put(start, 0.0);
        pq.add(start); // 시작 노드 추가

        while (!pq.isEmpty()) {
            RoadCenter current = pq.poll(); // 가장 가까운 노드 꺼내고

            if (current.equals(end)) break;

            // 현재 노드와 연결된 이웃 노드들 확인하기
            List<RoadLink> neighbors = graph.getOrDefault(current, new ArrayList<>());

            for (RoadLink link : neighbors) {
                RoadCenter neighbor = link.getTo();
                double alt = distance.get(current) + link.getDistance(); // 현재까지 거리 + 연결 거리

                // 만약 더 짧은 경로가 발견되면 갱신
                if (alt < distance.getOrDefault(neighbor, Double.MAX_VALUE)) {
                    distance.put(neighbor, alt);
                    previous.put(neighbor, current);
                    pq.add(neighbor);
                }
            }
        }

        // 도착지부터 이전 노드를 따라가며 리스트 구성
        List<RoadCenter> path = new ArrayList<>();
        RoadCenter step = end;
        while (step != null && previous.containsKey(step)) {
            path.add(step);
            step = previous.get(step);
        }
        if (step != null && step.equals(start)) path.add(start); // 출발점 포함

        Collections.reverse(path); // 역순 → 정방향 경로로 전환
        return path;
    }

    public RoadCenter getRoadCenterById(Long id) {
        return roadCenterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 도로 중심점이 없습니다."));
    }
}
