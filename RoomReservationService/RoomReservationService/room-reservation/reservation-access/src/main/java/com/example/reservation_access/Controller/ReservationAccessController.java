package com.example.reservation_access.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.reservation_access.Model.Reservation;
import com.example.reservation_access.Service.ReservationAccessService;

@RestController
@RequestMapping("/reservations")
public class ReservationAccessController {
    private final ReservationAccessService service;

    public ReservationAccessController(ReservationAccessService service) {
        this.service = service;
    }

    @PostMapping
    public Reservation create(@RequestBody Reservation reservation) {
        return service.save(reservation);
    }

    @GetMapping("/{id}")
    public Reservation findById(@PathVariable Long id) {
        return service.findById(id).orElseThrow();
    }

    @GetMapping
    public List<Reservation> findAll(
            @RequestParam(required = false) Long roomId,
            @RequestParam(required = false) String status) {
        return service.findAll(roomId, status);
    }

    @PatchMapping("/{id}/status")
    public Reservation updateStatus(@PathVariable Long id, @RequestBody String status) {
        return service.updateStatus(id, status).orElseThrow();
    }

    @PostMapping("/{id}/status")
    public Reservation updateStatusPost(@PathVariable Long id, @RequestBody String status) {
        return service.updateStatus(id, status).orElseThrow();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
