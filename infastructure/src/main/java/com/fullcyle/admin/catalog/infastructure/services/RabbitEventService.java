package com.fullcyle.admin.catalog.infastructure.services;

import org.springframework.amqp.rabbit.core.RabbitOperations;

import static com.fullcyle.admin.catalog.infastructure.configuration.json.Json.writeValueAsString;
import static java.util.Objects.requireNonNull;

public class RabbitEventService implements EventsService {

    private final String exchange;
    private final String routingKey;
    private final RabbitOperations ops;

    public RabbitEventService(final String exchange,
                              final String routingKey,
                              final RabbitOperations ops) {
        this.exchange = requireNonNull(exchange);
        this.routingKey = requireNonNull(routingKey);
        this.ops = requireNonNull(ops);
    }

    @Override
    public void send(final Object event) {
        this.ops.convertAndSend(this.exchange, this.routingKey, writeValueAsString(event));
    }
}
