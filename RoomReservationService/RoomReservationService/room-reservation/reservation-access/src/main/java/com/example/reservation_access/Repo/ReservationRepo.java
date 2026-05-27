package com.example.reservation_access.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.reservation_access.Model.Reservation;

public interface ReservationRepo extends JpaRepository<Reservation, Long> {
    List<Reservation> findByRoomId(Long roomId);

    List<Reservation> findByStatus(String status);

    List<Reservation> findByRoomIdAndStatus(Long roomId, String status);
}
