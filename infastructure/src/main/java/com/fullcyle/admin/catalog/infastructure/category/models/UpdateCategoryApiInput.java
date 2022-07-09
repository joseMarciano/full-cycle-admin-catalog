package com.fullcyle.admin.catalog.infastructure.category.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateCategoryApiInput(
        String name,
        String description,
        @JsonProperty("is_active") Boolean active
) {
}
