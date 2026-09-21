package com.dcm.backend.util;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TagNumberGenerator {

    private final JdbcTemplate jdbcTemplate;

    public Long generateTagNumber(String metalType) {

        if (metalType == null) {
            throw new IllegalArgumentException("Metal type cannot be null");
        }

        return switch (metalType.toUpperCase()) {

            case "GOLD" -> jdbcTemplate.queryForObject(
                    "SELECT nextval('gold_tag_sequence')",
                    Long.class
            );

            case "SILVER" -> jdbcTemplate.queryForObject(
                    "SELECT nextval('silver_tag_sequence')",
                    Long.class
            );

            default -> throw new IllegalArgumentException(
                    "Invalid metal type: " + metalType
            );
        };
    }
}