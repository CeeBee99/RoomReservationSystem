package com.example.overlap_engine.Service;

import com.example.overlap_engine.model.ReservationWindow;

import java.time.LocalDateTime;
import java.util.List;

public interface OverlapStrategy {
    List<Long> findConflicts(
            Long roomId,
            LocalDateTime requestedStart,
            LocalDateTime requestedEnd,
            List<ReservationWindow> existingReservations
    );
}
