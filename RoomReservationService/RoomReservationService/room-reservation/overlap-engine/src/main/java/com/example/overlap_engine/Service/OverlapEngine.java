package com.example.overlap_engine.Service;

import com.example.overlap_engine.model.OverlapCheckRequest;
import com.example.overlap_engine.model.OverlapCheckResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OverlapEngine {
    private final OverlapStrategy overlapStrategy;

    public OverlapEngine(OverlapStrategy overlapStrategy) {
        this.overlapStrategy = overlapStrategy;
    }

    public OverlapCheckResponse checkOverlap(OverlapCheckRequest request) {
        request.validateWindow();

        List<Long> conflicts = overlapStrategy.findConflicts(
                request.roomId(),
                request.requestedStart(),
                request.requestedEnd(),
                request.existingReservations()
        );

        if (conflicts.isEmpty()) {
            return new OverlapCheckResponse("CLEAR", List.of());
        }

        return new OverlapCheckResponse("CONFLICT", conflicts);
    }
}
