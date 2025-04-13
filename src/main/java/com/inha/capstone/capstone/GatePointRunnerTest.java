package com.inha.capstone.capstone;

import com.inha.capstone.capstone.entity.GatePoint;
import com.inha.capstone.capstone.repository.GatePointRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GatePointRunnerTest implements CommandLineRunner {

    private final GatePointRepository gatePointRepository;

    // 생성자 주입 방식
    public GatePointRunnerTest(GatePointRepository gatePointRepository) {
        this.gatePointRepository = gatePointRepository;
    }

    @Override
    public void run(String... args) {
        List<GatePoint> points = gatePointRepository.findAll();

        if (points.isEmpty()) {
            System.out.println("gate_points 테이블에 저장된 데이터가 없습니다.");
        } else {
            System.out.println("gate_points 테이블에 저장된 좌표 목록:");
            for (GatePoint point : points) {
                System.out.println("ID: " + point.getId()
                        + ", 위도: " + point.getLatitude()
                        + ", 경도: " + point.getLongitude());
            }
        }
    }
}