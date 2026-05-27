package com.example.roomreservationservice.demo.data;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.roomreservationservice.demo.model.Rooms;

public interface RoomsRepository extends JpaRepository<Rooms, Long> {
}
