package com.fullcyle.admin.catalog.infastructure.genre.persistence;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class GenreCategoryID implements Serializable {

    @Column(name = "ID_GENRES", nullable = false)
    private String genreId;

    @Column(name = "ID_CATEGORIES", nullable = false)
    private String categoryId;

    private GenreCategoryID() {
    }

    private GenreCategoryID(final String aGenreId, final String aCategoryId) {
        this.genreId = aGenreId;
        this.categoryId = aCategoryId;
    }

    public static GenreCategoryID from(final String aGenreId, final String aCategoryId) {
        return new GenreCategoryID(
                aGenreId,
                aCategoryId
        );
    }

    public String getGenreId() {
        return genreId;
    }

    public void setGenreId(String genreId) {
        this.genreId = genreId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final GenreCategoryID that = (GenreCategoryID) o;
        return getGenreId().equals(that.getGenreId()) && getCategoryId().equals(that.getCategoryId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGenreId(), getCategoryId());
    }
}
