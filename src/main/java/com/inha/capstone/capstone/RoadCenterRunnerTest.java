package com.inha.capstone.capstone;

import com.inha.capstone.capstone.entity.RoadCenter;
import com.inha.capstone.capstone.repository.RoadCenterRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoadCenterRunnerTest implements CommandLineRunner {

    private final RoadCenterRepository roadCenterRepository;

    public RoadCenterRunnerTest(RoadCenterRepository roadCenterRepository) {
        this.roadCenterRepository = roadCenterRepository;
    }

    @Override
    public void run(String... args) {
        List<RoadCenter> centers = roadCenterRepository.findAll();

        if (centers.isEmpty()) {
            System.out.println("road_centers 테이블에 저장된 데이터가 없습니다.");
        } else {
            System.out.println("road_centers 테이블에 저장된 도로 중심 좌표 목록:");
            for (RoadCenter rc : centers) {
                System.out.println("ID: " + rc.getId()
                        + ", 위도: " + rc.getLatitude()
                        + ", 경도: " + rc.getLongitude());
            }
        }
    }
}
