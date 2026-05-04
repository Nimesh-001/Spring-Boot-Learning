package com.lms.dto.request;

import com.lms.model.enums.Role;
import com.lms.model.enums.SubRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterUserRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 50, message = "Username must be 4-50 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @Email(message = "Valid email is required")
    private String email;

    private String phone;

    @NotNull(message = "Role is required")
    private Role role;

    /** Only relevant for TEACHER registrations. */
    private SubRole subRole = SubRole.NONE;

    private String department;

    private String registrationNumber;
}
