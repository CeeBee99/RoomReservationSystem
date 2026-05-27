package com.example.overlap_engine.model;

import java.util.List;

public record OverlapCheckResponse(
        String result,
        List<Long> conflictingReservationIds
) {
}
