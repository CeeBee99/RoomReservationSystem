package com.example.reservation_manager.messaging;

import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.stereotype.Component;

@Component
public class ReservationEventPublisher {
    private final RabbitOperations rabbitTemplate;

    public ReservationEventPublisher(RabbitOperations rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishReservationCreated(Long reservationId, Long roomId, Long userId, String status) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.RESERVATION_EXCHANGE,
                ReservationEventTopics.CREATED,
                reservationId + "," + roomId + "," + userId + "," + status);
    }

    public void publishReservationConfirmed(Long reservationId, Long roomId, Long userId) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.RESERVATION_EXCHANGE,
                ReservationEventTopics.CONFIRMED,
                reservationId + "," + roomId + "," + userId + ",CONFIRMED");
    }
}
