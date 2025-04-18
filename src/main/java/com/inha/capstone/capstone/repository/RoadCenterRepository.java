package com.inha.capstone.capstone.repository;

import com.inha.capstone.capstone.entity.RoadCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoadCenterRepository extends JpaRepository<RoadCenter, Long> {
}