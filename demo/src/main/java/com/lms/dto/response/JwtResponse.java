package com.lms.dto.response;

import com.lms.model.enums.Role;
import com.lms.model.enums.SubRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {

    private String token;
    @Builder.Default
    private String type = "Bearer";
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private Role role;
    private SubRole subRole;
}
