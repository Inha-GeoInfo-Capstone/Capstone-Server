package com.inha.capstone.capstone;  // 패키지 경로

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.inha.capstone.capstone.repository")
public class CapstoneApplication {
    public static void main(String[] args) {
        SpringApplication.run(CapstoneApplication.class, args);
    }
}