package com.example.event_processor.messaging;

import java.time.Instant;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.stereotype.Component;

@Component
public class ReservationConfirmedReceiver {
    private final RabbitOperations rabbitTemplate;

    public ReservationConfirmedReceiver(RabbitOperations rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.RESERVATION_CONFIRMED_QUEUE)
    public void receiveMessage(String message) {
        String[] parts = message.split(",");
        String reservationId = parts[0];
        String roomId = parts[1];
        String userId = parts[2];
        String timestamp = Instant.now().toString();

        System.out.println("Confirmation audit: reservationId=" + reservationId
                + ", roomId=" + roomId
                + ", userId=" + userId
                + ", timestamp=" + timestamp);

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.RESERVATION_EXCHANGE,
                RabbitMQConfig.CONFIRMATION_LOGGED_KEY,
                reservationId + "," + roomId + "," + userId + "," + timestamp);
    }
}
