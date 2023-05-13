package com.fullcyle.admin.catalog.infastructure.genre.persistence;

import com.fullcyle.admin.catalog.domain.category.CategoryID;
import com.fullcyle.admin.catalog.domain.genre.Genre;
import com.fullcyle.admin.catalog.domain.genre.GenreID;

import javax.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

@Entity(name = "Genre")
@Table(name = "GENRES")
public class GenreJpaEntity {

    @Id
    @Column(name = "ID", updatable = false)
    private String id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "FL_ACTIVE", nullable = false)
    private boolean active;

    @OneToMany(mappedBy = "genre", cascade = ALL, fetch = EAGER, orphanRemoval = true)
    private Set<GenreCategoryJpaEntity> categories;

    @Column(name = "CREATED_AT", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;

    @Column(name = "UPDATED_AT", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;

    @Column(name = "DELETED_AT", columnDefinition = "DATETIME(6)")
    private Instant deletedAt;

    public GenreJpaEntity() {
    }

    private GenreJpaEntity(final String anId,
                           final String aName,
                           final boolean isActive,
                           final Instant createdAt,
                           final Instant updatedAt,
                           final Instant deletedAt) {
        this.id = anId;
        this.name = aName;
        this.active = isActive;
        this.categories = new HashSet<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static GenreJpaEntity from(final Genre aGenre) {
        final var anEntity = new GenreJpaEntity(
                aGenre.getId().getValue(),
                aGenre.getName(),
                aGenre.isActive(),
                aGenre.getCreatedAt(),
                aGenre.getUpdatedAt(),
                aGenre.getDeletedAt()
        );

        aGenre.getCategories()
                .forEach(anEntity::addCategory);

        return anEntity;
    }

    public Genre toAggreate() {
        return Genre.with(
                GenreID.from(getId()),
                getName(),
                isActive(),
                getCategoryIDs(),
                getCreatedAt(),
                getUpdatedAt(),
                getDeletedAt()
        );
    }

    public List<CategoryID> getCategoryIDs() {
        return getCategories().stream().map(it -> CategoryID.from(it.getId().getCategoryId())).toList();
    }

    private void addCategory(final CategoryID anId) {
        this.categories.add(GenreCategoryJpaEntity.from(this, anId));
    }

    private void removeCategory(final CategoryID anId) {
        this.categories.remove(GenreCategoryJpaEntity.from(this, anId));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<GenreCategoryJpaEntity> getCategories() {
        return categories;
    }

    public void setCategories(Set<GenreCategoryJpaEntity> categories) {
        this.categories = categories;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }
}
