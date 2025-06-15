package com.example.assessment.dto;


import com.example.assessment.entity.Reservation.ReservationStatus;

public record CancelReservationResponse(
        Long reservationId,
        Long itemId,
        int  quantityCancelled,
        ReservationStatus status,
        int  availableAfterCancel
) {}

