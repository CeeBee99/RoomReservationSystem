package com.example.event_processor.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String RESERVATION_EXCHANGE = "reservation.exchange";
    public static final String RESERVATION_CONFIRMED_QUEUE = "event-processor.reservation-confirmed";
    public static final String RESERVATION_CONFIRMED_KEY = "reservation.confirmed";
    public static final String CONFIRMATION_LOGGED_KEY = "confirmation.logged";

    @Bean
    public TopicExchange reservationExchange() {
        return new TopicExchange(RESERVATION_EXCHANGE);
    }

    @Bean
    public Queue reservationConfirmedQueue() {
        return new Queue(RESERVATION_CONFIRMED_QUEUE);
    }

    @Bean
    public Binding reservationConfirmedBinding(Queue reservationConfirmedQueue, TopicExchange reservationExchange) {
        return BindingBuilder.bind(reservationConfirmedQueue)
                .to(reservationExchange)
                .with(RESERVATION_CONFIRMED_KEY);
    }
}
