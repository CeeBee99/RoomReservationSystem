package com.example.reservation_manager.Controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.reservation_manager.model.Reservation;
import com.example.reservation_manager.model.ReservationRequest;
import com.example.reservation_manager.Service.ReservationService;


@RestController
@RequestMapping("/reservations")
public class ReservationManagerController {
    private final ReservationService reservationService;

    public ReservationManagerController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public Reservation reserve(@RequestBody ReservationRequest request) {
        return reservationService.reserve(request);
    }

    @PostMapping("/{id}/confirm")
    public Reservation confirm(@PathVariable Long id) {
        return reservationService.confirm(id);
    }
}
