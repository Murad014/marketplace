package com.azercell.marketplace.catalog.domain.aggregate;

import com.azercell.marketplace.catalog.domain.vo.Status;
import com.azercell.marketplace.common.domain.ErrorCode;
import com.azercell.marketplace.common.exception.DomainException;
import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

/**
 * Category aggregate. Display text is stored bilingually (AZ + EN) — the public read picks the
 * locale-appropriate name/description, admins edit both. The {@code slug} is language-neutral (one
 * value, used in URLs). The parent link is an id ({@code parentId}, null = root); hierarchy invariants
 * (parent exists, no self/circular reference) are enforced by the application service.
 */
@Getter
public class Category {

    private final UUID id;
    private String nameAz;
    private String nameEn;
    private String slug;
    private String descriptionAz;
    private String descriptionEn;
    private Status status;
    private UUID parentId;   // null = root category

    private Category(UUID id, String nameAz, String nameEn, String slug,
                     String descriptionAz, String descriptionEn, Status status, UUID parentId) {
        this.id = validateId(id);
        this.nameAz = requireName(nameAz);
        this.nameEn = requireName(nameEn);
        this.slug = normalizeSlug(slug);
        this.descriptionAz = descriptionAz;
        this.descriptionEn = descriptionEn;
        this.status = status == null ? Status.ACTIVE : status;
        this.parentId = parentId;
    }

    public static Category create(String nameAz, String nameEn, String slug,
                                  String descriptionAz, String descriptionEn, UUID parentId) {
        return new Category(UUID.randomUUID(), nameAz, nameEn, slug,
                descriptionAz, descriptionEn, Status.ACTIVE, parentId);
    }

    public static Category rehydrate(UUID id, String nameAz, String nameEn, String slug,
                                     String descriptionAz, String descriptionEn, Status status, UUID parentId) {
        return new Category(id, nameAz, nameEn, slug, descriptionAz, descriptionEn, status, parentId);
    }

    public boolean isRoot() {
        return parentId == null;
    }

    public void changeName(String nameAz, String nameEn) {
        this.nameAz = requireName(nameAz);
        this.nameEn = requireName(nameEn);
    }

    public void changeSlug(String slug) {
        this.slug = normalizeSlug(slug);
    }

    public void changeDescription(String descriptionAz, String descriptionEn) {
        this.descriptionAz = descriptionAz;
        this.descriptionEn = descriptionEn;
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

    private static String requireName(String name) {
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
