package com.inha.capstone.capstone.service;

import com.inha.capstone.capstone.dto.GpsDataDTO;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GpsService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String FLASK_GPS_URL = "https://127.0.0.1/gps";

    public GpsDataDTO fetchCurrentLocation() {
        try {
            ResponseEntity<GpsDataDTO> response = restTemplate.exchange(
                    FLASK_GPS_URL,
                    HttpMethod.GET,
                    null,
                    GpsDataDTO.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new RuntimeException("GPS 데이터 수신 실패");
            }

        } catch (Exception e) {
            throw new RuntimeException("Flask GPS 서버와 통신 실패: " + e.getMessage(), e);
        }
    }
}