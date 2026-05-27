package com.example.roomreservationservice.demo.config;

import com.example.roomreservationservice.demo.model.Rooms;
import com.example.roomreservationservice.demo.data.RoomsRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final RoomsRepository roomsRepository;

    public DataLoader(RoomsRepository roomsRepository) {
        this.roomsRepository = roomsRepository;
    }

    @Override
    public void run(String... args) {
        if (roomsRepository.count() > 0) return;

        addRoom("Study Room", 6, true, true, true, true, true, true);
        addRoom("Business Meeting Room", 12, true, true, true, true, true, false);
        addRoom("Quiet Reading Room", 4, false, false, true, false, false, true);
        addRoom("Computer Lab Room", 20, true, true, true, true, true, false);
        addRoom("Small Discussion Room", 8, true, false, true, false, true, true);
    }

    private void addRoom(String name, int seats, boolean elevator, boolean projector,
                         boolean whiteboard, boolean computer, boolean table, boolean quiet) {

        Rooms room = new Rooms();
        room.setName(name);

        room.setSeats(seats);

        room.setElevatorAccess(elevator);
        room.setProjector(projector);
        room.setWhiteboard(whiteboard);
        room.setComputerAccess(computer);
        room.setLargeTable(table);
        room.setQuietRoom(quiet);
        room.setBooked(false);

        roomsRepository.save(room);
    }
}