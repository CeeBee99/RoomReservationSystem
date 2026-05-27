package com.example.roomreservationservice.demo.event;

import java.time.LocalDateTime;

public class ReservationConfirmedEvent {
    private Long reservationId;
    private Long roomId;
    private Long userId;
    private LocalDateTime timestamp;

    public ReservationConfirmedEvent() {}

    public ReservationConfirmedEvent(Long reservationId, Long roomId, Long userId, LocalDateTime timestamp) {
        this.reservationId = reservationId;
        this.roomId = roomId;
        this.userId = userId;
        this.timestamp = timestamp;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public Long getUserId() {
        return userId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}