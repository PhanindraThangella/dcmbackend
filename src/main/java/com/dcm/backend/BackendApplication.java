package com.dcm.backend;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import jakarta.annotation.PostConstruct;

/**
 * Main entry point for the Employee Management System application.
 *
 * <p>
 * Enables:
 * <ul>
 *     <li>Spring Boot auto-configuration</li>
 *     <li>Component scanning</li>
 *     <li>JPA Auditing for automatic entity auditing fields</li>
 * </ul>
 * </p>
 */
@SpringBootApplication
@EnableJpaAuditing
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
    }

}