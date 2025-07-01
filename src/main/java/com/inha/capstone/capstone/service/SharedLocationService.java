package com.inha.capstone.capstone.service;

import com.inha.capstone.capstone.entity.SharedLocation;
import com.inha.capstone.capstone.repository.SharedLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SharedLocationService {

    private final SharedLocationRepository repository;

    public Long createSharedLocation(double lat, double lng) {
        SharedLocation location = new SharedLocation();
        location.setLatitude(lat);
        location.setLongitude(lng);
        return repository.save(location).getId();
    }

    public SharedLocation getSharedLocation(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("공유 위치 ID가 존재하지 않습니다."));
    }
}