package com.example.reservation_manager.messaging;

public class ReservationCreatedEvent {
    private Long bookingId;
    private String status;

    public ReservationCreatedEvent(Long bookingId, String status){
        this.bookingId = bookingId;
        this.status = status;
    }
    public Long getBookingId() {
        return bookingId;
    }

    public String getStatus() {
        return status;
    }


}
