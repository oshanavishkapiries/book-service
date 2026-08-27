package com.bookstore.bookservice.controller;

import com.bookstore.bookservice.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class HealthController {

    private final BookService bookService;
    private final long startTime = System.currentTimeMillis();

    public HealthController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping({"/api/books/health", "/health"})
    public ResponseEntity<Map<String, Object>> getHealth() {
        Map<String, Object> health = new LinkedHashMap<>();
        health.put("status", "UP");
        health.put("service", "book-service");
        health.put("port", 8082);
        health.put("storage", "MongoDB / GCS (Ready)");
        health.put("totalBooks", bookService.findBooks(null, null).size());
        health.put("uptimeSeconds", (System.currentTimeMillis() - startTime) / 1000);
        health.put("jvmUptimeMs", ManagementFactory.getRuntimeMXBean().getUptime());
        health.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(health);
    }
}
