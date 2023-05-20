package com.fullcyle.admin.catalog.infastructure.configuration;

import com.fullcyle.admin.catalog.infastructure.configuration.properties.QueueProperties;
import com.fullcyle.admin.catalog.infastructure.services.EventsService;
import com.fullcyle.admin.catalog.infastructure.services.RabbitEventService;
import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventConfig {

    @Qualifier("videoCreatedEventService")
    @Bean
    public EventsService videoCreatedEventService(
            @Qualifier("videoCreatedProperties") final QueueProperties props,
            final RabbitOperations ops
    ) {
        return new RabbitEventService(props.getExchange(), props.getRoutingKey(), ops);
    }
}
