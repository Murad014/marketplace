package com.azercell.marketplace.users.infrastructure.security;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.UUID;

/**
 * Per-request cache of the resolved local user id. A single HTTP request can ask for the current
 * user several times (place order, then once per inventory movement, then the status history row);
 * without this they'd each re-resolve and re-hit the DB. We resolve once and memoize for the request,
 * so the {@code sub -> local user} lookup (and any JIT sync) runs at most once per request.
 * Request-scoped, so it's automatically cleared at the end of every request.
 */
@Component
@RequestScope
public class CurrentUserContext {

    @Getter
    private boolean resolved;
    private UUID userId; // nullable: a resolved-but-anonymous request caches null

    public UUID userId() {
        return userId;
    }

    public void resolve(UUID userId) {
        this.userId = userId;
        this.resolved = true;
    }
}
