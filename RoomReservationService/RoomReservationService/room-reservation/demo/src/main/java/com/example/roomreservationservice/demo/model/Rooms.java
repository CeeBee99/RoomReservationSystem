package com.example.roomreservationservice.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Rooms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int seats;

    private boolean elevatorAccess;
    private boolean projector;
    private boolean whiteboard;
    private boolean computerAccess;
    private boolean largeTable;
    private boolean quietRoom;

    private boolean booked;

    private LocalDateTime bookingStart;
    private LocalDateTime bookingEnd;

    public Rooms() {}

    public Rooms(String name, int seats, boolean elevatorAccess, boolean projector,
                 boolean whiteboard, boolean computerAccess, boolean largeTable,
                 boolean quietRoom) {
        this.name = name;
        this.seats = seats;
        this.elevatorAccess = elevatorAccess;
        this.projector = projector;
        this.whiteboard = whiteboard;
        this.computerAccess = computerAccess;
        this.largeTable = largeTable;
        this.quietRoom = quietRoom;
        this.booked = false;
    }

    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getSeats() { return seats; }
    public void setSeats(int seats) { this.seats = seats; }

    public boolean isElevatorAccess() { return elevatorAccess; }
    public void setElevatorAccess(boolean elevatorAccess) { this.elevatorAccess = elevatorAccess; }

    public boolean isProjector() { return projector; }
    public void setProjector(boolean projector) { this.projector = projector; }

    public boolean isWhiteboard() { return whiteboard; }
    public void setWhiteboard(boolean whiteboard) { this.whiteboard = whiteboard; }

    public boolean isComputerAccess() { return computerAccess; }
    public void setComputerAccess(boolean computerAccess) { this.computerAccess = computerAccess; }

    public boolean isLargeTable() { return largeTable; }
    public void setLargeTable(boolean largeTable) { this.largeTable = largeTable; }

    public boolean isQuietRoom() { return quietRoom; }
    public void setQuietRoom(boolean quietRoom) { this.quietRoom = quietRoom; }

    public boolean isBooked() { return booked; }
    public void setBooked(boolean booked) { this.booked = booked; }

    public LocalDateTime getBookingStart() { return bookingStart; }
    public void setBookingStart(LocalDateTime bookingStart) { this.bookingStart = bookingStart; }

    public LocalDateTime getBookingEnd() { return bookingEnd; }
    public void setBookingEnd(LocalDateTime bookingEnd) { this.bookingEnd = bookingEnd; }
}