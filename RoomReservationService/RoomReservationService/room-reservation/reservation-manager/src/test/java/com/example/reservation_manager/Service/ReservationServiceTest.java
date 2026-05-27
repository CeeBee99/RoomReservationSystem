package com.example.reservation_manager.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.reservation_manager.Client.OverlapEngineClient;
import com.example.reservation_manager.Client.ReservationAccessClient;
import com.example.reservation_manager.messaging.ReservationEventPublisher;
import com.example.reservation_manager.model.OverlapCheckRequest;
import com.example.reservation_manager.model.OverlapCheckResponse;
import com.example.reservation_manager.model.Reservation;
import com.example.reservation_manager.model.ReservationRequest;

class ReservationServiceTest {
    @Test
    void reserveAndConfirmUseAccessAndEvents() {
        TestClients clients = new TestClients("CLEAR");
        ReservationService service = new ReservationService(clients.overlap, clients.access, clients.publisher);

        Reservation saved = service.reserve(request());

        assertEquals("PENDING", saved.status());
        assertEquals(1, clients.access.createdCount);
        assertEquals("created:1,34,56,PENDING", clients.publisher.lastEvent);

        Reservation confirmed = service.confirm(1L);

        assertEquals("CONFIRMED", confirmed.status());
        assertEquals("confirmed:1,34,56", clients.publisher.lastEvent);
    }

    @Test
    void reserveDoesNotSaveWhenOverlapConflicts() {
        TestClients clients = new TestClients("CONFLICT");
        ReservationService service = new ReservationService(clients.overlap, clients.access, clients.publisher);

        assertThrows(RuntimeException.class, () -> service.reserve(request()));
        assertEquals(0, clients.access.createdCount);
        assertNull(clients.publisher.lastEvent);
    }

    private static ReservationRequest request() {
        return new ReservationRequest(
                34L,
                56L,
                LocalDateTime.parse("2026-05-01T10:00:00"),
                LocalDateTime.parse("2026-05-01T11:00:00"));
    }

    private static class TestClients {
        final FakeOverlapEngineClient overlap;
        final FakeReservationAccessClient access = new FakeReservationAccessClient();
        final FakeReservationEventPublisher publisher = new FakeReservationEventPublisher();

        TestClients(String overlapResult) {
            overlap = new FakeOverlapEngineClient(overlapResult);
        }
    }

    private static class FakeOverlapEngineClient extends OverlapEngineClient {
        private final String result;

        FakeOverlapEngineClient(String result) {
            super(null);
            this.result = result;
        }

        @Override
        public OverlapCheckResponse check(OverlapCheckRequest request) {
            return new OverlapCheckResponse(result, "CLEAR".equals(result) ? List.of() : List.of(99L));
        }
    }

    private static class FakeReservationAccessClient extends ReservationAccessClient {
        private int createdCount;

        FakeReservationAccessClient() {
            super(null);
        }

        @Override
        public List<Reservation> findByRoomId(Long roomId) {
            return List.of();
        }

        @Override
        public Reservation create(Reservation reservation) {
            createdCount++;
            return new Reservation(
                    1L,
                    reservation.roomId(),
                    reservation.userId(),
                    reservation.start(),
                    reservation.end(),
                    reservation.status());
        }

        @Override
        public Reservation updateStatus(Long id, String status) {
            return new Reservation(id, 34L, 56L, null, null, status);
        }
    }

    private static class FakeReservationEventPublisher extends ReservationEventPublisher {
        private String lastEvent;

        FakeReservationEventPublisher() {
            super(null);
        }

        @Override
        public void publishReservationCreated(Long reservationId, Long roomId, Long userId, String status) {
            lastEvent = "created:" + reservationId + "," + roomId + "," + userId + "," + status;
        }

        @Override
        public void publishReservationConfirmed(Long reservationId, Long roomId, Long userId) {
            lastEvent = "confirmed:" + reservationId + "," + roomId + "," + userId;
        }
    }
}
