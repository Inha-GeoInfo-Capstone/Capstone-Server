package com.inha.capstone.capstone.repository;

import com.inha.capstone.capstone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// api 테스트용
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}