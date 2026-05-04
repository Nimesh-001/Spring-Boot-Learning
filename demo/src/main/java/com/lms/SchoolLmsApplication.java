package com.lms;

import com.lms.model.User;
import com.lms.repository.UserRepository;
import com.lms.model.enums.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SchoolLmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchoolLmsApplication.class, args);
    }

    /**
     * Seeds the default admin account on first startup if none exists.
     */
    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!userRepository.existsByUsername("admin")) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("Admin@1234"));
                admin.setFirstName("System");
                admin.setLastName("Administrator");
                admin.setEmail("admin@school.lms");
                admin.setRole(Role.ADMIN);
                admin.setActive(true);
                userRepository.save(admin);
                System.out.println("✅ Default admin created  →  username: admin | password: Admin@1234");
            }
        };
    }
}
