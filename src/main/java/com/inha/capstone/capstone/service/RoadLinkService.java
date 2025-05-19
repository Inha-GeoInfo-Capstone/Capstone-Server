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

        double distance = calculateDistance(
                from.getLatitude(), from.getLongitude(),
                to.getLatitude(), to.getLongitude()
        );

        return roadLinkRepository.save(new RoadLink(from, to, distance));
    }

    public void connectSequentialCenters() {
        List<RoadCenter> centers = roadCenterRepository.findAll();

        for (int i = 0; i < centers.size() - 1; i++) {
            RoadCenter from = centers.get(i);
            RoadCenter to = centers.get(i + 1);

            double distance = calculateDistance(
                    from.getLatitude(), from.getLongitude(),
                    to.getLatitude(), to.getLongitude()
            );

            RoadLink forward = new RoadLink(from, to, distance);
            RoadLink backward = new RoadLink(to, from, distance); // 도로여서 양방향 연결로 바꿔야함

            roadLinkRepository.save(forward);
            roadLinkRepository.save(backward);
        }

        System.out.println("RoadLinks 자동 생성 및 거리 저장 완료");
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // 지구 반지름 (km)
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
