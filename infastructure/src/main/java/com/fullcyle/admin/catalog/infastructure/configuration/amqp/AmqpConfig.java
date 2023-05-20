package com.fullcyle.admin.catalog.infastructure.configuration.amqp;

import com.fullcyle.admin.catalog.infastructure.configuration.properties.QueueProperties;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfig {

    @ConfigurationProperties(prefix = "amqp.queues.video-created")
    @Qualifier("videoCreatedProperties")
    @Bean
    public QueueProperties videoCreatedProps() {
        return new QueueProperties();
    }

    @ConfigurationProperties(prefix = "amqp.queues.video-encoded")
    @Qualifier("videoEncodedProperties")
    @Bean
    public QueueProperties videoEncodedProps() {
        return new QueueProperties();
    }

    @Configuration
    static class Admin {
        @Qualifier("videoEvents")
        @Bean
        public Exchange videoEventsExchange(@Qualifier("videoCreatedProperties") QueueProperties properties) {
            return new DirectExchange(properties.getExchange());
        }

        @Qualifier("videoCreatedQueue")
        @Bean
        public Queue videoCreatedQueue(@Qualifier("videoCreatedProperties") QueueProperties properties) {
            return new Queue(properties.getQueue());
        }

        @Bean
        public Binding videoCreatedBinding(
                @Qualifier("videoEvents") DirectExchange exchange,
                @Qualifier("videoCreatedQueue") Queue queue,
                @Qualifier("videoCreatedProperties") QueueProperties properties
        ) {
            return BindingBuilder
                    .bind(queue)
                    .to(exchange)
                    .with(properties.getRoutingKey());
        }

        @Bean
        public Binding videoEncodedBinding(
                @Qualifier("videoEvents") DirectExchange exchange,
                @Qualifier("videoEncodedQueue") Queue queue,
                @Qualifier("videoEncodedProperties") QueueProperties properties
        ) {
            return BindingBuilder
                    .bind(queue)
                    .to(exchange)
                    .with(properties.getRoutingKey());
        }

        @Qualifier("videoEncodedQueue")
        @Bean
        public Queue videoEncodedQueue(@Qualifier("videoEncodedProperties") QueueProperties properties) {
            return new Queue(properties.getQueue());
        }

    }
}
