package com.example.assessment.service;

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
        // 2. Store reservation row
        Reservation res = reservationRepository.save(
                Reservation.builder()
                        .item(item)
                        .quantity(req.quantity())
                        .status(Reservation.ReservationStatus.RESERVED)
                        .build());

        // 3. Build response
        return new ReserveInventoryResponse(res.getReservationId(),
                req.itemId(),
                req.quantity(),
                res.getStatus(),
                availableAfter);
    }
}
