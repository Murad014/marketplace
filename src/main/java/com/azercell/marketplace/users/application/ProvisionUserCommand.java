package com.azercell.marketplace.users.application;

import com.azercell.marketplace.users.domain.vo.UserRole;

/** Identity data extracted from a token (or the dev fallback) used to provision/sync a local user. */
public record ProvisionUserCommand(
        String adObjectId,
        String employeeId,
        String email,
        String fullName,
        String department,
        UserRole role
) {
}