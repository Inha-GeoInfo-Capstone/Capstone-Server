package com.inha.capstone.capstone.repository;

import com.inha.capstone.capstone.entity.GatePoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GatePointRepository extends JpaRepository<GatePoint, Long> {

}
