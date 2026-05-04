package com.lms.controller;

import com.lms.dto.request.LoginRequest;
import com.lms.dto.response.ApiResponse;
import com.lms.dto.response.JwtResponse;
import com.lms.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * POST /api/auth/login
     * Public endpoint – authenticates a user and returns a JWT.
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        JwtResponse jwtResponse = authService.login(request);
        return ResponseEntity.ok(ApiResponse.ok("Login successful", jwtResponse));
    }
}
