package com.technomant.app.controllers;

import com.technomant.app.services.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<LocalDate, Long>> getStatistics() {
        Map<LocalDate, Long> statistics = adminService.getLastWeekStatistics();
        return ResponseEntity.ok(statistics);
    }
}
