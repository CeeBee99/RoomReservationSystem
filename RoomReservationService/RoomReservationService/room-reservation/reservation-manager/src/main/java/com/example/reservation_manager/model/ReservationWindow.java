package com.example.reservation_manager.model;

import java.time.LocalDateTime;

public record ReservationWindow(
        Long id,
        Long roomId,
        LocalDateTime start,
        LocalDateTime end,
        String status
) {
}
