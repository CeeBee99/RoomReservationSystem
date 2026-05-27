package com.example.overlap_engine.Service;

import com.example.overlap_engine.model.ReservationWindow;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class BufferOverlapStrategy implements OverlapStrategy {
    @Override
    public List<Long> findConflicts(
            Long roomId,
            LocalDateTime requestedStart,
            LocalDateTime requestedEnd,
            List<ReservationWindow> existingReservations
    ) {
        List<Long> conflicts = new ArrayList<>();

        if (existingReservations == null) {
            return conflicts;
        }

        for (ReservationWindow reservation : existingReservations) {
            if (reservation == null || reservation.roomId() == null || reservation.start() == null || reservation.end() == null) {
                continue;
            }

            if (!reservation.roomId().equals(roomId)) {
                continue;
            }

            if ("CANCELLED".equalsIgnoreCase(reservation.status()) || "REJECTED".equalsIgnoreCase(reservation.status())) {
                continue;
            }

            LocalDateTime bufferedStart = reservation.start().minusMinutes(15);
            LocalDateTime bufferedEnd = reservation.end().plusMinutes(15);

            if (requestedStart.isBefore(bufferedEnd) && requestedEnd.isAfter(bufferedStart)) {
                conflicts.add(reservation.id());
            }
        }

        return conflicts;
    }
}
