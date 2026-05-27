package com.example.reservation_manager.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.reservation_manager.Client.OverlapEngineClient;
import com.example.reservation_manager.Client.ReservationAccessClient;
import com.example.reservation_manager.messaging.ReservationEventPublisher;
import com.example.reservation_manager.model.OverlapCheckRequest;
import com.example.reservation_manager.model.OverlapCheckResponse;
import com.example.reservation_manager.model.Reservation;
import com.example.reservation_manager.model.ReservationRequest;
import com.example.reservation_manager.model.ReservationWindow;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class ReservationService {

    private static final String RESERVATION_ACCESS_CB = "reservationAccess";
    private static final String OVERLAP_ENGINE_CB = "overlapEngine";

    private final OverlapEngineClient overlapEngineClient;
    private final ReservationAccessClient reservationAccessClient;
    private final ReservationEventPublisher eventPublisher;

    public ReservationService(
            OverlapEngineClient overlapEngineClient,
            ReservationAccessClient reservationAccessClient,
            ReservationEventPublisher eventPublisher) {
        this.overlapEngineClient = overlapEngineClient;
        this.reservationAccessClient = reservationAccessClient;
        this.eventPublisher = eventPublisher;
    }

    @CircuitBreaker(name = RESERVATION_ACCESS_CB, fallbackMethod = "reserveFallback")
    public Reservation reserve(ReservationRequest request) {
        List<Reservation> existing = reservationAccessClient.findByRoomId(request.roomId());

        OverlapCheckResponse response = checkOverlap(new OverlapCheckRequest(
                request.roomId(),
                request.requestedStart(),
                request.requestedEnd(),
                existing.stream()
                        .map(r -> new ReservationWindow(r.id(), r.roomId(), r.start(), r.end(), r.status()))
                        .toList()));

        // If overlap engine is down, response will be SERVICE_UNAVAILABLE — refuse reservation
        if (response == null || "SERVICE_UNAVAILABLE".equals(response.result())) {
            throw new RuntimeException("Overlap engine unavailable — cannot confirm reservation is safe");
        }

        if (!"CLEAR".equals(response.result())) {
            throw new RuntimeException("Reservation conflicts with " + response.conflictingReservationIds());
        }

        Reservation saved = reservationAccessClient.create(new Reservation(
                null,
                request.roomId(),
                request.userId(),
                request.requestedStart(),
                request.requestedEnd(),
                "PENDING"));

        eventPublisher.publishReservationCreated(saved.id(), saved.roomId(), saved.userId(), saved.status());
        return saved;
    }

    @CircuitBreaker(name = OVERLAP_ENGINE_CB, fallbackMethod = "checkOverlapFallback")
    public OverlapCheckResponse checkOverlap(OverlapCheckRequest request) {
        return overlapEngineClient.check(request);
    }

    @CircuitBreaker(name = RESERVATION_ACCESS_CB, fallbackMethod = "confirmFallback")
    public Reservation confirm(Long id) {
        Reservation reservation = reservationAccessClient.updateStatus(id, "CONFIRMED");
        eventPublisher.publishReservationConfirmed(reservation.id(), reservation.roomId(), reservation.userId());
        return reservation;
    }


    // Returns a PENDING reservation with SERVICE_UNAVAILABLE status so the caller
    // knows the reservation was attempted but not persisted due to downstream failure
    public Reservation reserveFallback(ReservationRequest request, Throwable t) {
        return new Reservation(
                null,
                request.roomId(),
                request.userId(),
                request.requestedStart(),
                request.requestedEnd(),
                "SERVICE_UNAVAILABLE");
    }

    // Returns a response with result=SERVICE_UNAVAILABLE so the reserve() method
    // can detect the engine is down and refuse the reservation safely
    public OverlapCheckResponse checkOverlapFallback(OverlapCheckRequest request, Throwable t) {
        return new OverlapCheckResponse("SERVICE_UNAVAILABLE", List.of());
    }

    // Returns the reservation with CONFIRMATION_FAILED status so the caller
    // knows the confirm did not complete rather than receiving null
    public Reservation confirmFallback(Long id, Throwable t) {
        return new Reservation(id, null, null, null, null, "CONFIRMATION_FAILED");
    }
}
    
