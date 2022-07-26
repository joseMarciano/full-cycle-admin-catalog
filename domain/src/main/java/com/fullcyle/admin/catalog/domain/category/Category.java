package com.fullcyle.admin.catalog.domain.category;

import com.fullcyle.admin.catalog.domain.AggregateRoot;
import com.fullcyle.admin.catalog.domain.validation.ValidationHandler;

import java.time.Instant;
import java.util.Objects;

import static com.fullcyle.admin.catalog.domain.utils.InstantUtils.now;

public class Category extends AggregateRoot<CategoryID> implements Cloneable {

    private String name;
    private String description;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Category(
            final CategoryID anId,
            final String aName,
            final String aDescription,
            final boolean isActive,
            final Instant aCreationDate,
            final Instant aUpdateDate,
            final Instant aDeleteDate
    ) {
        super(anId);
        this.name = aName;
        this.description = aDescription;
        this.active = isActive;
        this.createdAt = Objects.requireNonNull(aCreationDate, "'createdAt' should not be null");
        this.updatedAt = Objects.requireNonNull(aUpdateDate, "'updatedAt' should not be null");
        this.deletedAt = aDeleteDate;
    }

    public static Category newCategory(final String aName, final String aDescription, final boolean isActive) {
        final var id = CategoryID.unique();
        final var now = now();
        final var deletedAt = isActive ? null : now;
        return new Category(id, aName, aDescription, isActive, now, now, deletedAt);
    }

    public static Category with(final CategoryID anId,
                                final String aName,
                                final String aDescription,
                                final boolean isActive,
                                final Instant createdAt,
                                final Instant updatedAt,
                                final Instant deletedAt) {
        return new Category(
                anId,
                aName,
                aDescription,
                isActive,
                createdAt,
                updatedAt,
                deletedAt
        );
    }

    @Override
    public void validate(ValidationHandler handler) {
        new CategoryValidator(this, handler).validate();
    }

    public Category deactivate() {
        var now = now();
        if (getDeletedAt() == null) {
            this.deletedAt = now;
        }

        this.active = false;
        this.updatedAt = now;
        return this;
    }

    public Category activate() {
        var now = now();
        this.deletedAt = null;
        this.active = true;
        this.updatedAt = now;
        return this;
    }

    public Category update(final String aName,
                           final String aDescription,
                           final boolean isActive) {

        if (isActive) activate();
        else deactivate();

        this.name = aName;
        this.description = aDescription;
        this.updatedAt = now();

        return this;
    }

    public CategoryID getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }


    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    @Override
    public Category clone() {
        try {
            return (Category) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
