package com.example.assessment.dto;

import com.example.assessment.entity.Reservation;

public record ReserveInventoryResponse(
        Long reservationId,
        Long itemId,
        int  quantity,
        Reservation.ReservationStatus status,
        int  availableAfterReserve
) {}
