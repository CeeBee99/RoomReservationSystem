package com.example.roomreservationservice.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.roomreservationservice.demo.data.RoomsRepository;
import com.example.roomreservationservice.demo.model.Rooms;
import com.example.roomreservationservice.demo.service.RoomsService;


@ExtendWith(MockitoExtension.class)
public class RoomsServiceCircuitBreakerTest {

    @Mock
    private RoomsRepository roomsRepository;

    @InjectMocks
    private RoomsService roomsService;

    // --- getAllRooms fallback ---
    // When the circuit breaker trips, return empty list instead of crashing

    @Test
    void whenCircuitBreakerTrips_getAllRooms_fallbackReturnsEmptyList() {
        List<Rooms> result = roomsService.getAllRoomsFallback(new RuntimeException("DB down"));

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    // --- getRoomById fallback ---
    // When the circuit breaker trips, return null instead of crashing

    @Test
    void whenCircuitBreakerTrips_getRoomById_fallbackReturnsNull() {
        Rooms result = roomsService.getRoomByIdFallback(1L, new RuntimeException("DB down"));

        assertThat(result).isNull();
    }

    // --- createRoom fallback ---
    // When the circuit breaker trips, return null instead of crashing

    @Test
    void whenCircuitBreakerTrips_createRoom_fallbackReturnsNull() {
        Rooms result = roomsService.createRoomFallback(new Rooms(), new RuntimeException("DB down"));

        assertThat(result).isNull();
    }

    // --- updateRoom fallback ---
    // When the circuit breaker trips, return null instead of crashing

    @Test
    void whenCircuitBreakerTrips_updateRoom_fallbackReturnsNull() {
        Rooms result = roomsService.updateRoomFallback(new Rooms(), new RuntimeException("DB down"));

        assertThat(result).isNull();
    }

    // --- deleteRoom fallback ---
    // When the circuit breaker trips, silently absorb failure instead of crashing

    @Test
    void whenCircuitBreakerTrips_deleteRoom_fallbackDoesNotThrow() {
        // Verifies no exception escapes the fallback
        roomsService.deleteRoomFallback(1L, new RuntimeException("DB down"));
    }

    // --- createRoom happy path ---
    // Verifies null reserved/notreserved lists are initialized before saving

    @Test
    void createRoom_initializesNullListsBeforeSaving() {
        Rooms room = new Rooms();
        assertThat(room.getReserved()).isNull();
        assertThat(room.getNotreserved()).isNull();

        when(roomsRepository.save(room)).thenReturn(room);
        roomsService.createRoom(room);

        assertThat(room.getReserved()).isNotNull();
        assertThat(room.getNotreserved()).isNotNull();
    }
}

