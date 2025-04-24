package com.inha.capstone.capstone.repository;

import com.inha.capstone.capstone.entity.RoadLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoadLinkRepository extends JpaRepository<RoadLink, Long> {
}