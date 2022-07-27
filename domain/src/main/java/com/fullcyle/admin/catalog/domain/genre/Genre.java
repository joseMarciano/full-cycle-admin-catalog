package com.fullcyle.admin.catalog.domain.genre;

import com.fullcyle.admin.catalog.domain.AggregateRoot;
import com.fullcyle.admin.catalog.domain.category.CategoryID;
import com.fullcyle.admin.catalog.domain.exceptions.NotificationException;
import com.fullcyle.admin.catalog.domain.validation.ValidationHandler;
import com.fullcyle.admin.catalog.domain.validation.handler.Notification;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.fullcyle.admin.catalog.domain.utils.InstantUtils.now;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

public class Genre extends AggregateRoot<GenreID> {

    private String name;
    private boolean active;
    private List<CategoryID> categories;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public static Genre newGenre(final String aName, final boolean isActive) {
        final var anId = GenreID.unique();
        final var now = now();
        final var deletedAt = isActive ? null : now;

        return new Genre(anId, aName, isActive, new ArrayList<>(), now, now, deletedAt);
    }

    public static Genre with(
            final GenreID anId,
            final String aName,
            final boolean isActive,
            final List<CategoryID> categories,
            final Instant aCreatedAt,
            final Instant aUpdatedAt,
            final Instant aDeletedAt) {
        return new Genre(anId, aName, isActive, categories, aCreatedAt, aUpdatedAt, aDeletedAt);
    }

    public static Genre with(final Genre aGenre) {
        return new Genre(
                aGenre.id,
                aGenre.name,
                aGenre.active,
                new ArrayList<>(aGenre.categories),
                aGenre.createdAt,
                aGenre.updatedAt,
                aGenre.deletedAt
        );
    }

    private Genre(
            final GenreID anId,
            final String aName,
            final boolean isActive,
            final List<CategoryID> categories,
            final Instant aCreatedAt,
            final Instant aUpdatedAt,
            final Instant aDeletedAt) {
        super(anId);

        this.name = aName;
        this.active = isActive;
        this.categories = categories;
        this.createdAt = aCreatedAt;
        this.updatedAt = aUpdatedAt;
        this.deletedAt = aDeletedAt;

        selfValidate();
    }

    @Override
    public void validate(final ValidationHandler anHandler) {
        new GenreValidator(this, anHandler).validate();
    }

    public Genre update(
            final String aName,
            final boolean isActive,
            final List<CategoryID> categories) {

        this.name = aName;
        this.categories = new ArrayList<>(ofNullable(categories).orElse(emptyList()));

        if (isActive) activate();
        else deactivate();

        selfValidate();
        return this;
    }

    public Genre deactivate() {
        final var now = now();
        this.active = false;
        if (getDeletedAt() == null) {
            this.deletedAt = now;
        }

        this.updatedAt = now;
        return this;
    }

    public Genre activate() {
        this.deletedAt = null;
        this.active = true;
        this.updatedAt = now();
        return this;
    }

    public Genre addCategory(final CategoryID aCategoryID) {
        if (aCategoryID == null) return this;

        this.categories.add(aCategoryID);
        this.updatedAt = now();
        return this;
    }
    public Genre addCategories(final List<CategoryID> categories) {
        if (categories == null || categories.isEmpty()) return this;

        this.categories.addAll(categories);
        this.updatedAt = now();
        return this;
    }

    public Genre removeCategory(final CategoryID aCategoryID) {
        if (aCategoryID == null) return this;

        this.categories.remove(aCategoryID);
        this.updatedAt = now();
        return this;
    }




    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public List<CategoryID> getCategories() {
        return Collections.unmodifiableList(categories);
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

    private void selfValidate() {
        final var notification = Notification.create();
        validate(notification);

        if (notification.hasErrors()) {
            throw new NotificationException("Failed to create a Aggregate Genre", notification);
        }
    }
}
