package com.inha.capstone.capstone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.inha.capstone.capstone.repository")
@EnableScheduling
public class CapstoneApplication {
    public static void main(String[] args) {
        SpringApplication.run(CapstoneApplication.class, args);
    }
}