package com.fullcyle.admin.catalog.domain.events;

@FunctionalInterface
public interface DomainEventPublisher<T extends DomainEvent> {
    void publishEvent(T event);
}
