package com.example.event_processor.messaging;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitOperations;

class ReservationConfirmedReceiverTest {
    @Test
    void repliesWithConfirmationLoggedEvent() {
        RabbitOperations rabbitTemplate = org.mockito.Mockito.mock(RabbitOperations.class);
        ReservationConfirmedReceiver receiver = new ReservationConfirmedReceiver(rabbitTemplate);

        receiver.receiveMessage("12,34,56,CONFIRMED");

        verify(rabbitTemplate).convertAndSend(
                eq(RabbitMQConfig.RESERVATION_EXCHANGE),
                eq(RabbitMQConfig.CONFIRMATION_LOGGED_KEY),
                startsWith("12,34,56,"));
    }
}
