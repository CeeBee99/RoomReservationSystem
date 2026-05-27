package com.example.roomreservationservice.demo.service;

import com.example.roomreservationservice.demo.data.RoomsRepository;
import com.example.roomreservationservice.demo.event.ReservationConfirmedEvent;
import com.example.roomreservationservice.demo.model.Rooms;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoomsService {

    private final RoomsRepository roomsRepository;
    private final ApplicationEventPublisher eventPublisher;

    public RoomsService(RoomsRepository roomsRepository,
                        ApplicationEventPublisher eventPublisher) {
        this.roomsRepository = roomsRepository;
        this.eventPublisher = eventPublisher;
    }

    public List<Rooms> getAllRooms() {
        try {
            return roomsRepository.findAll();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public Rooms getRoomById(Long id) {
        try {
            return roomsRepository.findById(id).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    public Rooms createRoom(Rooms room) {
        try {
            return roomsRepository.save(room);
        } catch (Exception e) {
            return null;
        }
    }

    public Rooms updateRoom(Rooms room) {
        try {
            return roomsRepository.save(room);
        } catch (Exception e) {
            return null;
        }
    }

    public Rooms saveRoom(Rooms room) {
        return createRoom(room);
    }

    public void deleteRoom(Long id) {
        try {
            roomsRepository.deleteById(id);
        } catch (Exception e) {
            // fallback: do not throw
        }
    }

    public void bookRoom(Long id, LocalDateTime startTime, LocalDateTime endTime) {
        try {
            Rooms room = roomsRepository.findById(id).orElseThrow();

            if (room.isBooked()) {
                System.out.println("Room is already booked.");
                return;
            }

            if (startTime == null || endTime == null || !endTime.isAfter(startTime)) {
                System.out.println("Invalid booking time.");
                return;
            }

            room.setBooked(true);
            room.setBookingStart(startTime);
            room.setBookingEnd(endTime);

            roomsRepository.save(room);

            eventPublisher.publishEvent(
                    new ReservationConfirmedEvent(
                            id,
                            id,
                            1L,
                            LocalDateTime.now()
                    )
            );

        } catch (Exception e) {
            // fallback: do not throw
        }
    }

    public void cancelBooking(Long id) {
        try {
            Rooms room = roomsRepository.findById(id).orElseThrow();

            room.setBooked(false);
            room.setBookingStart(null);
            room.setBookingEnd(null);

            roomsRepository.save(room);

            System.out.println("Booking cancelled for room: " + id);

        } catch (Exception e) {
            
        }
    }
}