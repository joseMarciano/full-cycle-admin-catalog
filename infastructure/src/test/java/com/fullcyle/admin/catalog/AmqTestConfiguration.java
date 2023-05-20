package com.fullcyle.admin.catalog;

import com.rabbitmq.client.Channel;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.test.RabbitListenerTest;
import org.springframework.amqp.rabbit.test.TestRabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Creates a proxy around each class annotated with @RabbitListener
 * that can be used to verify incoming messages via RabbitListenerTestHarness
 */
@Configuration
@RabbitListenerTest(spy = false, capture = true)
public class AmqTestConfiguration {

    @Bean
    public TestRabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        return new TestRabbitTemplate(connectionFactory);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(final ConnectionFactory connectionFactory) {
        final var factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        final var factory = Mockito.mock(ConnectionFactory.class);
        final var connection = Mockito.mock(Connection.class);
        final var channel = Mockito.mock(Channel.class);

        BDDMockito.willReturn(connection).given(factory).createConnection();
        BDDMockito.willReturn(channel).given(connection).createChannel(Mockito.anyBoolean());
        BDDMockito.given(channel.isOpen()).willReturn(true);


        return factory;
    }
}
