package com.lms.repository;

import com.lms.model.User;
import com.lms.model.enums.Role;
import com.lms.model.enums.SubRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> findByRole(Role role);

    List<User> findByRoleAndActive(Role role, boolean active);

    List<User> findBySubRole(SubRole subRole);

    // ── Report Queries ──────────────────────────────────────────────────────

    long countByRole(Role role);

    long countByActive(boolean active);

    long countByRoleAndActive(Role role, boolean active);

    @Query("SELECT COUNT(u) FROM User u WHERE u.active = true")
    long countActiveUsers();

    @Query("SELECT u.role, COUNT(u) FROM User u GROUP BY u.role")
    List<Object[]> countByRoleGrouped();

    @Query("SELECT u.subRole, COUNT(u) FROM User u WHERE u.role = 'TEACHER' GROUP BY u.subRole")
    List<Object[]> countTeachersBySubRole();

    @Query("SELECT u.department, COUNT(u) FROM User u WHERE u.role = 'TEACHER' AND u.department IS NOT NULL GROUP BY u.department")
    List<Object[]> countTeachersByDepartment();
}
