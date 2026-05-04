package com.lms.controller;

import com.lms.dto.request.AssignSubRoleRequest;
import com.lms.dto.request.RegisterUserRequest;
import com.lms.dto.response.ApiResponse;
import com.lms.dto.response.UserResponse;
import com.lms.model.enums.Role;
import com.lms.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // ── Register Users ─────────────────────────────────────────────────────

    /**
     * POST /api/admin/users/register
     * Admin registers a new user (any role).
     */
    @PostMapping("/users/register")
    public ResponseEntity<ApiResponse> registerUser(
            @Valid @RequestBody RegisterUserRequest request,
            Authentication authentication) {
        UserResponse user = adminService.registerUser(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("User registered successfully", user));
    }

    // ── Assign Sub-Role ────────────────────────────────────────────────────

    /**
     * PATCH /api/admin/teachers/{teacherId}/assign-sub-role
     * Assign / change the school-hierarchy sub-role for a teacher.
     */
    @PatchMapping("/teachers/{teacherId}/assign-sub-role")
    public ResponseEntity<ApiResponse> assignSubRole(
            @PathVariable Long teacherId,
            @Valid @RequestBody AssignSubRoleRequest request) {
        UserResponse updated = adminService.assignSubRole(teacherId, request);
        return ResponseEntity.ok(ApiResponse.ok("Sub-role assigned successfully", updated));
    }

    // ── List Users ─────────────────────────────────────────────────────────

    /**
     * GET /api/admin/users — list all users
     */
    @GetMapping("/users")
    public ResponseEntity<ApiResponse> getAllUsers() {
        List<UserResponse> users = adminService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.ok("Users retrieved", users));
    }

    /**
     * GET /api/admin/users/{id} — get single user by id
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long id) {
        UserResponse user = adminService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.ok("User retrieved", user));
    }

    /**
     * GET /api/admin/users/role/{role} — filter by role
     */
    @GetMapping("/users/role/{role}")
    public ResponseEntity<ApiResponse> getUsersByRole(@PathVariable String role) {
        List<UserResponse> users = adminService.getUsersByRole(Role.valueOf(role.toUpperCase()));
        return ResponseEntity.ok(ApiResponse.ok("Users retrieved", users));
    }

    /**
     * GET /api/admin/teachers — all teachers (convenience shortcut)
     */
    @GetMapping("/teachers")
    public ResponseEntity<ApiResponse> getTeachers() {
        List<UserResponse> teachers = adminService.getUsersByRole(Role.TEACHER);
        return ResponseEntity.ok(ApiResponse.ok("Teachers retrieved", teachers));
    }

    /**
     * GET /api/admin/students — all students
     */
    @GetMapping("/students")
    public ResponseEntity<ApiResponse> getStudents() {
        List<UserResponse> students = adminService.getUsersByRole(Role.STUDENT);
        return ResponseEntity.ok(ApiResponse.ok("Students retrieved", students));
    }

    /**
     * GET /api/admin/technical-officers — all technical officers
     */
    @GetMapping("/technical-officers")
    public ResponseEntity<ApiResponse> getTechnicalOfficers() {
        List<UserResponse> officers = adminService.getUsersByRole(Role.TECHNICAL_OFFICER);
        return ResponseEntity.ok(ApiResponse.ok("Technical officers retrieved", officers));
    }

    // ── Toggle Status ──────────────────────────────────────────────────────

    /**
     * PATCH /api/admin/users/{id}/toggle-status — activate / deactivate
     */
    @PatchMapping("/users/{id}/toggle-status")
    public ResponseEntity<ApiResponse> toggleStatus(@PathVariable Long id) {
        UserResponse updated = adminService.toggleUserStatus(id);
        String msg = updated.isActive() ? "User activated" : "User deactivated";
        return ResponseEntity.ok(ApiResponse.ok(msg, updated));
    }

    // ── Delete ─────────────────────────────────────────────────────────────

    /**
     * DELETE /api/admin/users/{id}
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.ok("User deleted successfully"));
    }
}
