package com.lms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Consolidated admin report response containing all statistics sections.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponse {

    /** Total number of registered users. */
    private long totalUsers;

    /** Active vs inactive breakdown. */
    private long activeUsers;
    private long inactiveUsers;

    /** Per-role totals. */
    private long totalAdmins;
    private long totalTeachers;
    private long totalStudents;
    private long totalTechnicalOfficers;

    /** Teachers broken down by sub-role (key = SubRole name, value = count). */
    private Map<String, Long> teachersBySubRole;

    /** Teachers broken down by department (key = department name, value = count). */
    private Map<String, Long> teachersByDepartment;

    /** Active user counts per role. */
    private Map<String, Long> activeUsersByRole;
}
