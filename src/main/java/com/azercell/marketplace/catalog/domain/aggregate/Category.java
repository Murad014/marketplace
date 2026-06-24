package com.azercell.marketplace.catalog.domain.aggregate;

import com.azercell.marketplace.catalog.domain.vo.Status;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
public class Category {

    private final UUID id;
    private String slug;
    private String name;
    private String description;
    private Status status;

    private Category parent;
    private final List<Category> children;

    public Category(
            UUID id,
            String name,
            String slug,
            String description,
            Status status,
            Category parent,
            List<Category> children) {

        validateId(id);
        validateName(name);
        validateSlug(slug);

        this.id = id;
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.status = status == null ? Status.ACTIVE : status;
        this.parent = parent;
        this.children = children == null
                ? new ArrayList<>()
                : new ArrayList<>(children);
    }

    public boolean isRoot() {
        return parent == null;
    }

    public void changeSlug(String slug) {
        validateSlug(slug);
        this.slug = slug;
    }

    public void changeName(String name) {
        validateName(name);
        this.name = name;
    }

    public void changeDescription(String description) {
        this.description = description;
    }

    public void addChild(Category child) {
        Objects.requireNonNull(child, "Child category cannot be null");

        if (this.id.equals(child.id))
            throw new IllegalArgumentException("Category cannot be child of itself");

        validateDuplicateChild(child);
        validateCircularReference(child);

        child.parent = this;
        this.children.add(child);
    }

    public void removeChild(UUID childId) {
        children.removeIf(child -> child.getId().equals(childId));
    }

    public void makeActive() {
        this.status = Status.ACTIVE;
    }

    public void makeInactive() {
        this.status = Status.IN_ACTIVE;
    }

    // ---------------------------------------------------------
    // Validation
    // ---------------------------------------------------------

    private void validateId(UUID id) {
        if (id == null)
            throw new IllegalArgumentException("Category id cannot be null");
    }

    private void validateName(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Category name cannot be empty");
    }

    private void validateSlug(String slug) {
        if (slug == null || slug.isBlank())
            throw new IllegalArgumentException("Category slug cannot be empty");
    }

    private void validateDuplicateChild(Category child) {
        boolean duplicate =
                children.stream()
                        .anyMatch(existing ->
                                existing.getId().equals(child.getId()));

        if (duplicate)
            throw new IllegalArgumentException(
                    "Child category already exists: " + child.getId());
    }

    private void validateCircularReference(Category child) {
        Category current = this;

        while (current != null) {
            if (current.getId().equals(child.getId()))
                throw new IllegalArgumentException(
                        "Circular category hierarchy detected");

            current = current.parent;
        }
    }
}