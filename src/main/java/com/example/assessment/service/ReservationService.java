package com.example.assessment.service;

import com.example.assessment.dto.CancelReservationResponse;
import com.example.assessment.dto.ReserveInventoryRequest;
import com.example.assessment.dto.ReserveInventoryResponse;
import com.example.assessment.entity.Item;
import com.example.assessment.entity.Reservation;
import com.example.assessment.repository.ItemRepository;
import com.example.assessment.repository.ReservationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final InventoryService inventoryService;
    private final ReservationRepository reservationRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public ReserveInventoryResponse reserve(ReserveInventoryRequest req) {

        Item item = itemRepository.findById(req.itemId())
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));

        int availableAfter = inventoryService.reserveStock(item.getItemId(), req.quantity());

        Reservation res = reservationRepository.save(
                Reservation.builder()
                        .item(item)
                        .quantity(req.quantity())
                        .status(Reservation.ReservationStatus.RESERVED)
                        .build());

        return new ReserveInventoryResponse(res.getReservationId(),
                req.itemId(),
                req.quantity(),
                res.getStatus(),
                availableAfter);
    }

    @Transactional
    public CancelReservationResponse cancelReservation(Long reservationId) {

        Reservation res = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Reservation " + reservationId + " not found"));

        if (res.getStatus() != Reservation.ReservationStatus.RESERVED) {
            throw new IllegalStateException("Reservation " + reservationId + " is already " + res.getStatus());
        }

        int qty = res.getQuantity();
        Long itemId = res.getItem().getItemId();

        int availableAfter = inventoryService.releaseStock(itemId, qty);

        res.setStatus(Reservation.ReservationStatus.CANCELLED);
        reservationRepository.save(res);

        return new CancelReservationResponse(
                res.getReservationId(),
                itemId,
                qty,
                res.getStatus(),
                availableAfter
        );
    }
}
