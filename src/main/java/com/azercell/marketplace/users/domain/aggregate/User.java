package com.azercell.marketplace.users.domain.aggregate;

import com.azercell.marketplace.common.domain.ErrorCode;
import com.azercell.marketplace.common.exception.DomainException;
import com.azercell.marketplace.users.domain.vo.UserRole;
import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

/**
 * Local mirror of an identity owned by Keycloak (RHSSO). {@code adObjectId} is the OIDC {@code sub}
 * claim — the stable join key. Authentication/credentials live in Keycloak; this is a projection
 * for relational integrity (orders/reviews FK here) and app-specific data, kept fresh via JIT sync.
 */
@Getter
public class User {

    private final UUID id;
    private final String adObjectId;   // OIDC sub — immutable identity key (keycloakId)
    private String employeeId;
    private String email;
    private String fullName;
    private String department;
    private UserRole role;
    private boolean active;

    private User(UUID id, String adObjectId, String employeeId, String email, String fullName,
                 String department, UserRole role, boolean active) {
        this.id = id;
        this.adObjectId = adObjectId;
        this.employeeId = employeeId;
        this.email = email;
        this.fullName = fullName;
        this.department = department;
        this.role = role;
        this.active = active;
    }

    public static User provision(String adObjectId, String employeeId, String email, String fullName,
                                 String department, UserRole role) {
        if (adObjectId == null || adObjectId.isBlank())
            throw new DomainException(ErrorCode.INVALID_ARGUMENT);
        return new User(UUID.randomUUID(), adObjectId, employeeId, email, fullName, department,
                role == null ? UserRole.EMPLOYEE : role, true);
    }

    public static User rehydrate(UUID id, String adObjectId, String employeeId, String email,
                                 String fullName, String department, UserRole role, boolean active) {
        return new User(id, adObjectId, employeeId, email, fullName, department, role, active);
    }

    /**
     * Reconcile mutable profile fields against the latest identity-provider claims (Keycloak can change
     * name/email/department/role). Only non-null claims are considered. Returns {@code true} iff something
     * actually changed — callers use this to skip a needless DB write on the common no-op request.
     */
    public boolean applyIdentity(String employeeId, String email, String fullName, String department, UserRole role) {
        boolean changed = false;
        if (employeeId != null && !employeeId.equals(this.employeeId)) { this.employeeId = employeeId; changed = true; }
        if (email != null && !email.equals(this.email)) { this.email = email; changed = true; }
        if (fullName != null && !fullName.equals(this.fullName)) { this.fullName = fullName; changed = true; }
        if (department != null && !department.equals(this.department)) { this.department = department; changed = true; }
        if (role != null && role != this.role) { this.role = role; changed = true; }
        return changed;
    }

    public void deactivate() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}