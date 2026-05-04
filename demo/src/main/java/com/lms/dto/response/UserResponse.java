package com.lms.dto.response;

import com.lms.model.enums.Role;
import com.lms.model.enums.SubRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Role role;
    private SubRole subRole;
    private String department;
    private String registrationNumber;
    private boolean active;
    private String createdBy;
    private LocalDateTime createdAt;
}
