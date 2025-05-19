package com.inha.capstone.capstone.initializer;

import com.inha.capstone.capstone.entity.RoadCenter;
import com.inha.capstone.capstone.entity.RoadLink;
import com.inha.capstone.capstone.repository.RoadCenterRepository;
import com.inha.capstone.capstone.repository.RoadLinkRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RoadLinkInitializer {

    private final RoadCenterRepository roadCenterRepository;
    private final RoadLinkRepository roadLinkRepository;

    @PostConstruct
    public void initializeRoadLinks() {
        List<RoadCenter> centers = roadCenterRepository.findAll();

        if (centers.size() < 2) {
            System.out.println("⚠️ 도로 중심점이 충분하지 않습니다.");
            return;
        }

        // 기존에 생성된 링크가 있다면 생략 (중복 방지)
        if (!roadLinkRepository.findAll().isEmpty()) {
            System.out.println("ℹ️ 이미 RoadLink 데이터가 존재합니다. 생성을 건너뜁니다.");
            return;
        }

        for (int i = 0; i < centers.size() - 1; i++) {
            RoadCenter from = centers.get(i);
            RoadCenter to = centers.get(i + 1);

            double distance = calculateDistance(
                    from.getLatitude(), from.getLongitude(),
                    to.getLatitude(), to.getLongitude()
            );

            RoadLink forward = new RoadLink(from, to, distance);
            RoadLink backward = new RoadLink(to, from, distance); // 양방향 연결

            roadLinkRepository.save(forward);
            roadLinkRepository.save(backward);
        }

        System.out.println("✅ RoadLink 자동 생성 완료!");
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}

