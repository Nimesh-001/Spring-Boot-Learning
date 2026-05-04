package com.lms.controller;

import com.lms.dto.response.ApiResponse;
import com.lms.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    /**
     * GET /api/reports/full
     * Full consolidated admin report.
     */
    @GetMapping("/full")
    public ResponseEntity<ApiResponse> getFullReport() {
        return ResponseEntity.ok(ApiResponse.ok("Full report retrieved", reportService.getFullReport()));
    }

    /**
     * GET /api/reports/role-summary
     * User count grouped by role (for dashboard cards/charts).
     */
    @GetMapping("/role-summary")
    public ResponseEntity<ApiResponse> getRoleSummary() {
        return ResponseEntity.ok(ApiResponse.ok("Role summary retrieved", reportService.getRoleSummary()));
    }

    /**
     * GET /api/reports/active-status
     * Active vs inactive users per role.
     */
    @GetMapping("/active-status")
    public ResponseEntity<ApiResponse> getActiveStatus() {
        return ResponseEntity.ok(ApiResponse.ok("Active status report retrieved",
                reportService.getActiveStatusByRole()));
    }
}
