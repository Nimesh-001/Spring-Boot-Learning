package com.lms.service;

import com.lms.dto.response.ReportResponse;
import com.lms.model.enums.Role;
import com.lms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final UserRepository userRepository;

    /**
     * Returns a full consolidated report for the admin dashboard.
     */
    public ReportResponse getFullReport() {
        long total       = userRepository.count();
        long active      = userRepository.countActiveUsers();
        long inactive    = total - active;
        long admins      = userRepository.countByRole(Role.ADMIN);
        long teachers    = userRepository.countByRole(Role.TEACHER);
        long students    = userRepository.countByRole(Role.STUDENT);
        long techOff     = userRepository.countByRole(Role.TECHNICAL_OFFICER);

        // Teachers by sub-role
        Map<String, Long> bySubRole = new HashMap<>();
        userRepository.countTeachersBySubRole()
                .forEach(row -> bySubRole.put(row[0].toString(), (Long) row[1]));

        // Teachers by department
        Map<String, Long> byDept = new HashMap<>();
        userRepository.countTeachersByDepartment()
                .forEach(row -> byDept.put(row[0].toString(), (Long) row[1]));

        // Active users per role
        Map<String, Long> activeByRole = new HashMap<>();
        for (Role role : Role.values()) {
            activeByRole.put(role.name(), userRepository.countByRoleAndActive(role, true));
        }

        return ReportResponse.builder()
                .totalUsers(total)
                .activeUsers(active)
                .inactiveUsers(inactive)
                .totalAdmins(admins)
                .totalTeachers(teachers)
                .totalStudents(students)
                .totalTechnicalOfficers(techOff)
                .teachersBySubRole(bySubRole)
                .teachersByDepartment(byDept)
                .activeUsersByRole(activeByRole)
                .build();
    }

    /**
     * Per-role summary for quick widgets on the dashboard.
     */
    public Map<String, Long> getRoleSummary() {
        Map<String, Long> summary = new HashMap<>();
        userRepository.countByRoleGrouped()
                .forEach(row -> summary.put(row[0].toString(), (Long) row[1]));
        return summary;
    }

    /**
     * All users with their active/inactive split per role.
     */
    public Map<String, Map<String, Long>> getActiveStatusByRole() {
        Map<String, Map<String, Long>> result = new HashMap<>();
        for (Role role : Role.values()) {
            Map<String, Long> split = new HashMap<>();
            split.put("active",   userRepository.countByRoleAndActive(role, true));
            split.put("inactive", userRepository.countByRoleAndActive(role, false));
            result.put(role.name(), split);
        }
        return result;
    }
}
