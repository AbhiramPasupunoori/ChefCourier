package com.chefcourier.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health/database")
public class DatabaseHealthController {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseHealthController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> checkDatabase() {
        Integer result = jdbcTemplate.queryForObject(
                "SELECT 1",
                Integer.class
        );

        boolean connected = result != null && result == 1;

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("application", "ChefCourier");
        response.put("database", "MySQL");
        response.put("status", connected ? "UP" : "DOWN");

        return ResponseEntity.ok(response);
    }
}
