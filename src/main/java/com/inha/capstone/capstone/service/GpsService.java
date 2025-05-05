package com.inha.capstone.capstone.service;

import com.inha.capstone.capstone.dto.GpsDataDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GpsService {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://127.0.0.1") // Flask 서버 주소
            .build();

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
