package com.example.reservation_access.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.reservation_access.Model.Reservation;
import com.example.reservation_access.Repo.ReservationRepo;

@Service
public class ReservationAccessService {
    private final ReservationRepo repo;

    public ReservationAccessService(ReservationRepo repo) {
        this.repo = repo;
    }

    public Reservation save(Reservation reservation) {
        return repo.save(reservation);
    }

    public Optional<Reservation> findById(Long id) {
        return repo.findById(id);
    }

    public List<Reservation> findAll(Long roomId, String status) {
        if (roomId != null && status != null) {
            return repo.findByRoomIdAndStatus(roomId, status);
        }
        if (roomId != null) {
            return repo.findByRoomId(roomId);
        }
        if (status != null) {
            return repo.findByStatus(status);
        }
        return repo.findAll();
    }

    public Optional<Reservation> updateStatus(Long id, String status) {
        return repo.findById(id).map(reservation -> {
            reservation.setStatus(status);
            return repo.save(reservation);
        });
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
