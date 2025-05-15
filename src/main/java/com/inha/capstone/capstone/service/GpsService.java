package com.inha.capstone.capstone.service;

import com.inha.capstone.capstone.dto.GpsDataDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GpsService {

    private final WebClient webClient;

    public GpsService(WebClient webClient) {
        this.webClient = webClient;
    }

    public GpsDataDTO fetchCurrentLocation() {
        try {
            Mono<GpsDataDTO> response = webClient.get()
                    .uri("/gps")
                    .retrieve()
                    .bodyToMono(GpsDataDTO.class);

            GpsDataDTO data = response.block(); // 동기 방식
            if (data != null) {
                System.out.printf("Flask에서 수신한 위치: 위도 %.7f, 경도 %.7f, SNR %.14f\n",
                        data.getLatitude(), data.getLongitude(), data.getSnr());
            } else {
                System.out.println("Flask에서 데이터를 받았지만 내용이 없음.");
            }

            return data;
        } catch (Exception e) {
            System.err.println("Flask 서버에서 위치 정보 수신 실패: " + e.getMessage());
            throw new RuntimeException("Flask 서버와 통신 실패", e);
        }
    }
}
