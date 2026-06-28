package com.azercell.marketplace.users.infrastructure.security;

import com.azercell.marketplace.common.security.CurrentUserProvider;
import com.azercell.marketplace.users.application.ProvisionUserCommand;
import com.azercell.marketplace.users.application.service.UserService;
import com.azercell.marketplace.users.domain.vo.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Resolves the current request's local user id: provisions/syncs from the Keycloak JWT when present.
 * In local dev (no token) a seeded dev user is used so actor stamping keeps working — that fallback
 * is off by default and only enabled via {@code app.security.dev-user-fallback}.
 *
 * <p>Resolution is memoized per request via {@link CurrentUserContext}, so even when several flows in
 * one request ask for the current user, the {@code sub -> local user} lookup (and any JIT sync) runs
 * once. JIT find-or-create is what guarantees the local row exists before any order/review FK insert —
 * it cannot be replaced by async Keycloak events, which can't promise existence at write time.
 */
@Component
@RequiredArgsConstructor
public class CurrentUserProviderImpl implements CurrentUserProvider {

    private static final ProvisionUserCommand DEV_USER = new ProvisionUserCommand(
            "local-dev-user", "DEV-0001", "dev@azercell.local", "Local Dev", "Engineering", UserRole.ADMIN);

    private final UserService userService;
    private final CurrentUserContext currentUserContext;

    @Value("${app.security.dev-user-fallback:false}")
    private boolean devUserFallback;

    @Override
    public Optional<UUID> currentUserId() {
        // Outside an HTTP request (e.g. a scheduled job) there is no request scope to memoize into,
        // so resolve directly without caching.
        if (RequestContextHolder.getRequestAttributes() == null) {
            return resolve();
        }
        if (!currentUserContext.isResolved()) {
            currentUserContext.resolve(resolve().orElse(null));
        }
        return Optional.ofNullable(currentUserContext.userId());
    }

    private Optional<UUID> resolve() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            return Optional.of(userService.provision(fromJwt(jwtAuth.getToken())).getId());
        }
        if (devUserFallback) {
            return Optional.of(userService.provision(DEV_USER).getId());
        }
        return Optional.empty();
    }

    private ProvisionUserCommand fromJwt(Jwt jwt) {
        return new ProvisionUserCommand(
                jwt.getSubject(),
                jwt.getClaimAsString("preferred_username"),
                jwt.getClaimAsString("email"),
                jwt.getClaimAsString("name"),
                jwt.getClaimAsString("department"),
                highestRole(jwt));
    }

    @SuppressWarnings("unchecked")
    private UserRole highestRole(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess != null && realmAccess.get("roles") instanceof Collection<?> roles) {
            boolean admin = roles.stream().map(Object::toString).anyMatch(r -> r.equalsIgnoreCase("ADMIN"));
            return admin ? UserRole.ADMIN : UserRole.EMPLOYEE;
        }
        return UserRole.EMPLOYEE;
    }
}
