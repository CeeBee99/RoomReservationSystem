package com.example.reservation_manager.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ConfirmationLoggedReceiver {
    @RabbitListener(queues = RabbitMQConfig.CONFIRMATION_LOGGED_QUEUE)
    public void receiveMessage(String message) {
        System.out.println("Confirmation logged: " + message);
    }
}
