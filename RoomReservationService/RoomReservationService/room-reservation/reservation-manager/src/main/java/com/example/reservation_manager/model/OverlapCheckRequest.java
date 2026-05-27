package com.example.reservation_manager.model;

import java.time.LocalDateTime;
import java.util.List;

public record OverlapCheckRequest(
        Long roomId,
        LocalDateTime requestedStart,
        LocalDateTime requestedEnd,
        List<ReservationWindow> existingReservations
) {
}
