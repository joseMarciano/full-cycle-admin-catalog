package com.fullcyle.admin.catalog.domain;

import com.fullcyle.admin.catalog.domain.events.DomainEvent;

import java.util.List;

import static java.util.Collections.emptyList;

public abstract class AggregateRoot<ID extends Identifier> extends Entity<ID> {

    protected AggregateRoot(final ID id) {
        this(id, emptyList());
    }

    protected AggregateRoot(final ID id, final List<DomainEvent> domainEvents) {
        super(id, domainEvents);
    }
}
