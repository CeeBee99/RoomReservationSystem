package com.example.reservation_manager.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String RESERVATION_EXCHANGE = "reservation.exchange";
    public static final String CONFIRMATION_LOGGED_QUEUE = "reservation-manager.confirmation-logged";

    @Bean
    public TopicExchange reservationExchange(){
        return new TopicExchange(RESERVATION_EXCHANGE);
    }

    @Bean
    public Queue confirmationLoggedQueue() {
        return new Queue(CONFIRMATION_LOGGED_QUEUE);
    }

    @Bean
    public Binding confirmationLoggedBinding(Queue confirmationLoggedQueue, TopicExchange reservationExchange) {
        return BindingBuilder.bind(confirmationLoggedQueue)
                .to(reservationExchange)
                .with(ReservationEventTopics.CONFIRMATION_LOGGED);
    }
}
