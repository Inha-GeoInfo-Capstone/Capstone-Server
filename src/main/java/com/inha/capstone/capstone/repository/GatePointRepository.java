package com.inha.capstone.capstone.repository;

import com.inha.capstone.capstone.entity.GatePoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GatePointRepository extends JpaRepository<GatePoint, Long> {
    Optional<GatePoint> findByName(String name);
}
