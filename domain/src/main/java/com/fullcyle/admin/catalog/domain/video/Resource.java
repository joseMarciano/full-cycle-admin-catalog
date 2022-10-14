package com.fullcyle.admin.catalog.domain.video;

import com.fullcyle.admin.catalog.domain.ValueObject;

import static java.util.Objects.requireNonNull;

public class Resource extends ValueObject {

    private final byte[] content;
    private final String contentType;
    private final String name;
    private final Type type;

    private Resource(final byte[] content,
                     final String contentType,
                     final String name,
                     final Type type) {
        this.content = requireNonNull(content);
        this.contentType = requireNonNull(contentType);
        this.name = requireNonNull(name);
        this.type = requireNonNull(type);
    }

    public static Resource with(
            final byte[] content,
            final String contentType,
            final String name,
            final Type type
    ) {
        return new Resource(content, contentType, name, type);
    }

    public enum Type {
        VIDEO,
        TRAILER,
        BANNER,
        THUMBNAIL,
        THUMBNAIL_HALF
    }

    public byte[] content() {
        return content;
    }

    public String contentType() {
        return contentType;
    }

    public String name() {
        return name;
    }

    public Type type() {
        return type;
    }
}
