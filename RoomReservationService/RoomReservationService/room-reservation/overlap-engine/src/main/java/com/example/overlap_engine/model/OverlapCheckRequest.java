package com.example.overlap_engine.model;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public record OverlapCheckRequest(
        @NotNull Long roomId,
        @NotNull LocalDateTime requestedStart,
        @NotNull LocalDateTime requestedEnd,
        List<ReservationWindow> existingReservations
) {
    public void validateWindow() {
        if (!requestedStart.isBefore(requestedEnd)) {
            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "requestedStart must be before requestedEnd"
            );
        }
    }
}
