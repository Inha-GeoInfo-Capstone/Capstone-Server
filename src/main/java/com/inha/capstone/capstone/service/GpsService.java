package com.inha.capstone.capstone.service;

import com.inha.capstone.capstone.dto.GpsDataDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GpsService {

    private final WebClient webClient;
    private final GpsLocationHolder gpsHolder;

    public GpsService(WebClient webClient, GpsLocationHolder gpsHolder) {
        this.webClient = webClient;
        this.gpsHolder = gpsHolder;
    }

    public GpsDataDTO fetchCurrentLocation() {
        try {
            Mono<GpsDataDTO> response = webClient.get()
                    .uri("/gps")
                    .retrieve()
                    .bodyToMono(GpsDataDTO.class);

            GpsDataDTO data = response.block(); // 동기 방식으로 수신
            if (data != null) {
                gpsHolder.update(data.getLatitude(), data.getLongitude(), data.getSnr());

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

    // 메모리에 저장된 위치를 꺼내오는 함수
    public GpsDataDTO getLastLocation() {
        return gpsHolder.getCurrent();
    }
}
