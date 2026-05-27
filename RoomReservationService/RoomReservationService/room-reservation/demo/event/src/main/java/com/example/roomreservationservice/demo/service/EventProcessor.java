package com.example.roomreservationservice.demo.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EventProcessor {

    private final ApplicationEventPublisher publisher;

    public EventProcessor(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @EventListener
    public void handleReservationConfirmed(ReservationConfirmedEvent event) {
        System.out.println("EVENT RECEIVED: ReservationConfirmed");
        System.out.println("Logging confirmation audit for reservation ID: " + event.getReservationId());

        ConfirmationLoggedEvent confirmationLoggedEvent =
                new ConfirmationLoggedEvent(
                        event.getReservationId(),
                        event.getRoomId(),
                        event.getUserId(),
                        LocalDateTime.now()
                );

        publisher.publishEvent(confirmationLoggedEvent);

        System.out.println("EVENT PUBLISHED: ConfirmationLogged");
    }
}