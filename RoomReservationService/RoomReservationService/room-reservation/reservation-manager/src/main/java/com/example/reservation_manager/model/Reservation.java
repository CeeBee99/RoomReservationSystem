package com.example.reservation_manager.model;

import java.time.LocalDateTime;

public record Reservation(
        Long id,
        Long roomId,
        Long userId,
        LocalDateTime start,
        LocalDateTime end,
        String status
) {
}
