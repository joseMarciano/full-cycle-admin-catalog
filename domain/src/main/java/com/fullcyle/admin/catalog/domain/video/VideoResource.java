package com.fullcyle.admin.catalog.domain.video;

import com.fullcyle.admin.catalog.domain.ValueObject;

import java.util.Objects;

public class VideoResource extends ValueObject {

    private final VideoMediaType type;
    private final Resource resource;

    public static VideoResource with(final Resource resource, final VideoMediaType mediaType) {
        return new VideoResource(mediaType, resource);
    }

    private VideoResource(final VideoMediaType type, final Resource resource) {
        this.type = Objects.requireNonNull(type);
        this.resource = Objects.requireNonNull(resource);
    }

    public VideoMediaType type() {
        return type;
    }

    public Resource resource() {
        return resource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final VideoResource that = (VideoResource) o;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}
