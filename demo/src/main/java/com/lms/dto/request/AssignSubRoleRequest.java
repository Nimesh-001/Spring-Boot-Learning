package com.lms.dto.request;

import com.lms.model.enums.SubRole;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignSubRoleRequest {

    @NotNull(message = "SubRole is required")
    private SubRole subRole;

    private String department;
}
