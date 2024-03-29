package com.fullcyle.admin.catalog.domain.video;

import com.fullcyle.admin.catalog.domain.ValueObject;

import static java.util.Objects.requireNonNull;

public class Resource extends ValueObject {

    private final String checksum;
    private final byte[] content;
    private final String contentType;
    private final String name;

    private Resource(
            final String checksum,
            final byte[] content,
            final String contentType,
            final String name) {
        this.checksum = requireNonNull(checksum);
        this.content = requireNonNull(content);
        this.contentType = requireNonNull(contentType);
        this.name = requireNonNull(name);
    }

    public static Resource with(
            final String checksum,
            final byte[] content,
            final String contentType,
            final String name
    ) {
        return new Resource(checksum, content, contentType, name);
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

    public String checksum() {
        return checksum;
    }

}
