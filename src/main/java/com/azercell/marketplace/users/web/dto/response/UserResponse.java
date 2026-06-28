package com.azercell.marketplace.users.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Local mirror of a Keycloak-owned user")
public record UserResponse(
        @Schema(description = "Local user id") UUID id,
        @Schema(description = "OIDC sub claim (identity key)") String adObjectId,
        @Schema(description = "Employee id", nullable = true) String employeeId,
        @Schema(description = "Email", example = "dev@azercell.local") String email,
        @Schema(description = "Full name", example = "Local Dev") String fullName,
        @Schema(description = "Department", nullable = true) String department,
        @Schema(description = "Role", example = "ADMIN", allowableValues = {"EMPLOYEE", "ADMIN"}) String role,
        @Schema(description = "Whether the local mirror is active", example = "true") boolean active
) {
}
