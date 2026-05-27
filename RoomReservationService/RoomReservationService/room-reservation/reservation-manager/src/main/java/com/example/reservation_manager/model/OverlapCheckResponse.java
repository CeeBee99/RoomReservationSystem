package com.example.reservation_manager.model;

import java.util.List;

public record OverlapCheckResponse(
        String result,
        List<Long> conflictingReservationIds
) {
}
