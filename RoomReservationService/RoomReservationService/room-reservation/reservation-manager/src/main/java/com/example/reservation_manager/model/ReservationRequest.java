package com.example.reservation_manager.model;

import java.time.LocalDateTime;

public record ReservationRequest(
        Long roomId,
        Long userId,
        LocalDateTime requestedStart,
        LocalDateTime requestedEnd
) {
}
