package com.fullcyle.admin.catalog.infastructure.genre.persistence;

import com.fullcyle.admin.catalog.domain.category.CategoryID;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "GENRES_CATEGORIES")
public class GenreCategoryJpaEntity {

    @EmbeddedId
    private GenreCategoryID id;

    @ManyToOne
    @JoinColumn(name = "ID_GENRES")
    @MapsId("genreId")
    private GenreJpaEntity genre;

    public GenreCategoryJpaEntity() {
    }

    private GenreCategoryJpaEntity(final GenreJpaEntity aGenre, final CategoryID aCategoryId) {
        this.id = GenreCategoryID.from(aGenre.getId(), aCategoryId.getValue());
        this.genre = aGenre;
    }

    public static GenreCategoryJpaEntity from(final GenreJpaEntity aGenre, final CategoryID aCategoryId) {
        return new GenreCategoryJpaEntity(aGenre, aCategoryId);
    }


    public GenreCategoryID getId() {
        return id;
    }

    public void setId(GenreCategoryID id) {
        this.id = id;
    }

    public GenreJpaEntity getGenre() {
        return genre;
    }

    public void setGenre(GenreJpaEntity genre) {
        this.genre = genre;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final GenreCategoryJpaEntity that = (GenreCategoryJpaEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
