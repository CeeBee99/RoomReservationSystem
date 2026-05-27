package com.example.overlap_engine.Service;

import com.example.overlap_engine.model.OverlapCheckRequest;
import com.example.overlap_engine.model.OverlapCheckResponse;
import com.example.overlap_engine.model.ReservationWindow;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BufferOverlapStrategyTest {
    private final OverlapEngine overlapEngine = new OverlapEngine(new BufferOverlapStrategy());

    @Test
    void returnsConflictInsideFifteenMinuteBuffer() {
        OverlapCheckRequest request = new OverlapCheckRequest(
                10L,
                LocalDateTime.of(2026, 4, 23, 11, 5),
                LocalDateTime.of(2026, 4, 23, 12, 0),
                List.of(
                        new ReservationWindow(1L, 10L,
                                LocalDateTime.of(2026, 4, 23, 10, 0),
                                LocalDateTime.of(2026, 4, 23, 11, 0),
                                "CONFIRMED")
                )
        );

        OverlapCheckResponse response = overlapEngine.checkOverlap(request);

        assertEquals("CONFLICT", response.result());
        assertEquals(List.of(1L), response.conflictingReservationIds());
    }

    @Test
    void returnsTwoConflictsWhenBetweenTwoBufferedReservations() {
        OverlapCheckRequest request = new OverlapCheckRequest(
                10L,
                LocalDateTime.of(2026, 4, 23, 10, 50),
                LocalDateTime.of(2026, 4, 23, 11, 10),
                List.of(
                        new ReservationWindow(1L, 10L,
                                LocalDateTime.of(2026, 4, 23, 10, 0),
                                LocalDateTime.of(2026, 4, 23, 10, 40),
                                "CONFIRMED"),
                        new ReservationWindow(2L, 10L,
                                LocalDateTime.of(2026, 4, 23, 11, 20),
                                LocalDateTime.of(2026, 4, 23, 12, 0),
                                "CONFIRMED")
                )
        );

        OverlapCheckResponse response = overlapEngine.checkOverlap(request);

        assertEquals("CONFLICT", response.result());
        assertEquals(List.of(1L, 2L), response.conflictingReservationIds());
    }
}
