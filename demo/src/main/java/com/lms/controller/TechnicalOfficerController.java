package com.lms.controller;

import com.lms.dto.response.ApiResponse;
import com.lms.dto.response.UserResponse;
import com.lms.exception.ResourceNotFoundException;
import com.lms.model.User;
import com.lms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/technical")
@PreAuthorize("hasRole('TECHNICAL_OFFICER') or hasRole('ADMIN')")
@RequiredArgsConstructor
public class TechnicalOfficerController {

    private final UserRepository userRepository;

    /**
     * GET /api/technical/dashboard
     * Returns the authenticated technical officer's profile.
     */
    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse> getDashboard(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        UserResponse response = UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .subRole(user.getSubRole())
                .registrationNumber(user.getRegistrationNumber())
                .active(user.isActive())
                .createdAt(user.getCreatedAt())
                .build();

        return ResponseEntity.ok(ApiResponse.ok("Technical officer dashboard loaded", response));
    }
}
