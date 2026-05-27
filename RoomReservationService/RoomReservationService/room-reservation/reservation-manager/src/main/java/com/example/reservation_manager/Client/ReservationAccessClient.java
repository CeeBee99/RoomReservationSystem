package com.example.reservation_manager.Client;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.reservation_manager.model.Reservation;

@Component
public class ReservationAccessClient {
    private final RestTemplate rest;
    private final String url = "http://reservation-access:8080/reservations";

    public ReservationAccessClient(RestTemplate rest) {
        this.rest = rest;
    }

    public List<Reservation> findByRoomId(Long roomId) {
        Reservation[] reservations = rest.getForObject(url + "?roomId=" + roomId, Reservation[].class);
        return Arrays.asList(reservations);
    }

    public Reservation create(Reservation reservation) {
        return rest.postForObject(url, reservation, Reservation.class);
    }

    public Reservation updateStatus(Long id, String status) {
        return rest.postForObject(url + "/" + id + "/status", status, Reservation.class);
    }
}
