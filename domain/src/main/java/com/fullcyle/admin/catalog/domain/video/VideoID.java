package com.fullcyle.admin.catalog.domain.video;

import com.fullcyle.admin.catalog.domain.Identifier;
import com.fullcyle.admin.catalog.domain.utils.IdUtils;

import java.util.Objects;

public class VideoID extends Identifier {
    private final String value;

    private VideoID(final String value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    public static VideoID unique() {
        return VideoID.from(IdUtils.uuid());
    }

    public static VideoID from(final String anId) {
        return new VideoID(anId.toLowerCase());
    }


    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoID that = (VideoID) o;
        return getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
