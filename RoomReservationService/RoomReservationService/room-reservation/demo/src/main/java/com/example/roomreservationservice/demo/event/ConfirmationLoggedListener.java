package com.example.roomreservationservice.demo.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ConfirmationLoggedListener {

    @EventListener
    public void handleConfirmationLogged(ConfirmationLoggedEvent event) {
        System.out.println("Confirmation logged for reservation: " + event.getReservationId());
    }
}