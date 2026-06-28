package com.azercell.marketplace.common.security;

import java.util.Optional;
import java.util.UUID;

/**
 * Cross-cutting access to the current authenticated user's local id, so any context can stamp
 * actor fields (order.userId, movement.performedBy, status changedBy) without depending on the
 * users domain. Implemented by the users context against the security context + JIT provisioning.
 */
public interface CurrentUserProvider {

    Optional<UUID> currentUserId();

    default UUID requireCurrentUserId() {
        return currentUserId().orElseThrow(
                () -> new IllegalStateException("No authenticated user in context"));
    }
}