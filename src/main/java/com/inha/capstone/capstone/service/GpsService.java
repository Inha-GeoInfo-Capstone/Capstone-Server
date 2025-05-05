package com.inha.capstone.capstone.service;

import com.inha.capstone.capstone.dto.GpsDataDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GpsService {

    private final WebClient webClient;

    // 의존성 주입 방식으로 코드 변경
    public GpsService(WebClient webClient) {
        this.webClient = webClient;
    }


    public GpsDataDTO fetchCurrentLocation() {
        try {
            Mono<GpsDataDTO> response = webClient.get()
                    .uri("/gps")
                    .retrieve()
                    .bodyToMono(GpsDataDTO.class);

            return response.block();
        } catch (Exception e) {
            throw new RuntimeException("Flask 서버와 통신 실패: " + e.getMessage(), e);
        }
    }
}
