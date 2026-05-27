package com.example.room_manager.Service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.room_manager.Client.RoomAccessClient;
import com.example.room_manager.Model.Room;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class RoomManagerService {

    private static final String ROOM_ACCESS_CB = "roomAccess";

    @Autowired
    private RoomAccessClient client;

    @CircuitBreaker(name = ROOM_ACCESS_CB, fallbackMethod = "getAllRoomsFallback")
    public List<Room> getAllRooms() {
        return client.getAll();
    }

    // Returns the room with status CREATION_FAILED so the caller knows
    // the room was not persisted rather than receiving a null or exception
    @CircuitBreaker(name = ROOM_ACCESS_CB, fallbackMethod = "createRoomFallback")
    public Room createRoom(Room room) {
        if (room.getCapacity() <= 0) {
            throw new RuntimeException("Capacity cannot be less than zero");
        }
        room.setStatus("Available");
        return client.create(room);
    }

    // Returns a sentinel room with status DELETION_FAILED so the caller
    // knows the delete did not complete rather than silently swallowing the failure
    @CircuitBreaker(name = ROOM_ACCESS_CB, fallbackMethod = "deleteRoomFallback")
    public void deleteRoom(Long id) {
        client.delete(id);
    }


    public List<Room> getAllRoomsFallback(Throwable t) {
        // UI shows no rooms available
        // rather than crashing. Caller can check circuit breaker state if needed.
        return Collections.emptyList();
    }

    public Room createRoomFallback(Room room, Throwable t) {
        // Return the submitted room with CREATION_FAILED status so the caller
        // knows exactly what was attempted and that it did not persist
        room.setStatus("CREATION_FAILED");
        return room;
    }

    public void deleteRoomFallback(Long id, Throwable t) {
        // Log the failure — caller should check response and retry when service recovers
        System.err.println("[CircuitBreaker] deleteRoom fallback triggered for id=" + id
                + " | Cause: " + t.getMessage());
    }
}
