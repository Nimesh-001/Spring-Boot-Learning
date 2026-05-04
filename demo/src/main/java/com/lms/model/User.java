package com.lms.model;

import com.lms.model.enums.Role;
import com.lms.model.enums.SubRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Core user entity shared across all roles.
 * Discriminated by the {@link Role} enum; teachers also carry a {@link SubRole}.
 */
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @NotBlank
    @Column(nullable = false, length = 80)
    private String firstName;

    @NotBlank
    @Column(nullable = false, length = 80)
    private String lastName;

    @Email
    @Column(unique = true, length = 100)
    private String email;

    @Column(length = 15)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    /**
     * Only meaningful for TEACHER role. Defaults to NONE for all others.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private SubRole subRole = SubRole.NONE;

    /** Department / subject area (mainly for teachers). */
    @Column(length = 100)
    private String department;

    /** Admission/staff number shown on the dashboard. */
    @Column(length = 50)
    private String registrationNumber;

    @Column(nullable = false)
    private boolean active = true;

    /** Created-by tracking (admin username). */
    @Column(length = 50)
    private String createdBy;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
