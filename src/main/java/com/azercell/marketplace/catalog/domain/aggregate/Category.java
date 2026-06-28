package com.azercell.marketplace.catalog.domain.aggregate;

import com.azercell.marketplace.catalog.domain.vo.Status;
import com.azercell.marketplace.common.domain.ErrorCode;
import com.azercell.marketplace.common.exception.DomainException;
import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

/**
 * Category aggregate. The parent link is kept as an id ({@code parentId}, null = root) rather than a
 * loaded subtree — the hierarchy invariants (parent exists, no self/circular reference) are enforced
 * by the application service, which can walk ancestors via the repository.
 */
@Getter
public class Category {

    private final UUID id;
    private String name;
    private String slug;
    private String description;
    private Status status;
    private UUID parentId;   // null = root category

    private Category(UUID id, String name, String slug, String description, Status status, UUID parentId) {
        this.id = validateId(id);
        this.name = normalizeName(name);
        this.slug = normalizeSlug(slug);
        this.description = description;
        this.status = status == null ? Status.ACTIVE : status;
        this.parentId = parentId;
    }

    public static Category create(String name, String slug, String description, UUID parentId) {
        return new Category(UUID.randomUUID(), name, slug, description, Status.ACTIVE, parentId);
    }

    public static Category rehydrate(UUID id, String name, String slug, String description,
                                     Status status, UUID parentId) {
        return new Category(id, name, slug, description, status, parentId);
    }

    public boolean isRoot() {
        return parentId == null;
    }

    public void changeName(String name) {
        this.name = normalizeName(name);
    }

    public void changeSlug(String slug) {
        this.slug = normalizeSlug(slug);
    }

    public void changeDescription(String description) {
        this.description = description;
    }

    /** Re-parent (null = make root). Self-reference is rejected here; circular checks live in the service. */
    public void changeParent(UUID parentId) {
        if (parentId != null && parentId.equals(this.id))
            throw new DomainException(ErrorCode.CATEGORY_SELF_PARENT);
        this.parentId = parentId;
    }

    public void makeActive() {
        this.status = Status.ACTIVE;
    }

    public void makeInactive() {
        this.status = Status.IN_ACTIVE;
    }

    // <editor-fold desc="privateHelperMethods">
    private static UUID validateId(UUID id) {
        if (id == null) throw new DomainException(ErrorCode.CATEGORY_ID_REQUIRED);
        return id;
    }

    private static String normalizeName(String name) {
        if (name == null || name.isBlank())
            throw new DomainException(ErrorCode.CATEGORY_NAME_REQUIRED);
        return name.trim();
    }

    private static String normalizeSlug(String slug) {
        if (slug == null || slug.isBlank())
            throw new DomainException(ErrorCode.CATEGORY_SLUG_REQUIRED);
        return slug.trim().toLowerCase();
    }
    // </editor-fold>

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
