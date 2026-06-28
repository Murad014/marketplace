package com.azercell.marketplace.users.web.dto.response;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String adObjectId,
        String employeeId,
        String email,
        String fullName,
        String department,
        String role,
        boolean active
) {
}