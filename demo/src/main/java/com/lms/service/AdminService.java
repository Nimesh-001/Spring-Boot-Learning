package com.lms.service;

import com.lms.dto.request.AssignSubRoleRequest;
import com.lms.dto.request.RegisterUserRequest;
import com.lms.dto.response.UserResponse;
import com.lms.exception.BadRequestException;
import com.lms.exception.DuplicateResourceException;
import com.lms.exception.ResourceNotFoundException;
import com.lms.model.User;
import com.lms.model.enums.Role;
import com.lms.model.enums.SubRole;
import com.lms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ── Register ───────────────────────────────────────────────────────────

    @Transactional
    public UserResponse registerUser(RegisterUserRequest request, String createdBy) {
        // Validate uniqueness
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username '" + request.getUsername() + "' is already taken");
        }
        if (request.getEmail() != null && userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email '" + request.getEmail() + "' is already registered");
        }

        // SubRole is only valid for teachers
        SubRole subRole = request.getSubRole() != null ? request.getSubRole() : SubRole.NONE;
        if (subRole != SubRole.NONE && request.getRole() != Role.TEACHER) {
            throw new BadRequestException("SubRole can only be assigned to TEACHER role");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRole(request.getRole());
        user.setSubRole(subRole);
        user.setDepartment(request.getDepartment());
        user.setRegistrationNumber(request.getRegistrationNumber());
        user.setActive(true);
        user.setCreatedBy(createdBy);

        return toResponse(userRepository.save(user));
    }

    // ── Assign SubRole ─────────────────────────────────────────────────────

    @Transactional
    public UserResponse assignSubRole(Long teacherId, AssignSubRoleRequest request) {
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + teacherId));

        if (teacher.getRole() != Role.TEACHER) {
            throw new BadRequestException("SubRole can only be assigned to TEACHER role");
        }

        teacher.setSubRole(request.getSubRole());
        if (request.getDepartment() != null) {
            teacher.setDepartment(request.getDepartment());
        }

        return toResponse(userRepository.save(teacher));
    }

    // ── Queries ────────────────────────────────────────────────────────────

    public List<UserResponse> getUsersByRole(Role role) {
        return userRepository.findByRole(role).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return toResponse(user);
    }

    // ── Toggle Active Status ───────────────────────────────────────────────

    @Transactional
    public UserResponse toggleUserStatus(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        user.setActive(!user.isActive());
        return toResponse(userRepository.save(user));
    }

    // ── Delete ─────────────────────────────────────────────────────────────

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
    }

    // ── Mapper ─────────────────────────────────────────────────────────────

    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .subRole(user.getSubRole())
                .department(user.getDepartment())
                .registrationNumber(user.getRegistrationNumber())
                .active(user.isActive())
                .createdBy(user.getCreatedBy())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
