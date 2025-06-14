package com.example.assessment.service;

import com.example.assessment.dto.InventoryDto;
import com.example.assessment.dto.UpdateInventoryRequest;
import com.example.assessment.entity.Inventory;
import com.example.assessment.entity.Item;
import com.example.assessment.exception.OutOfStockException;
import com.example.assessment.mapper.InventoryMapper;
import com.example.assessment.repository.InventoryRepository;
import com.example.assessment.repository.ItemRepository;
import com.example.assessment.repository.ReservationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ItemRepository itemRepository;
    private final InventoryMapper inventoryMapper;
    private final ReservationRepository reservationRepository;
//    private final CacheManager cacheManager;

    //    @Autowired
//    public InventoryService(
//            InventoryRepository inventoryRepository,
//            ItemRepository itemRepository,
//            InventoryMapper inventoryMapper
//    ) {
//        this.inventoryRepository = inventoryRepository;
//        this.itemRepository = itemRepository;
//        this.inventoryMapper = inventoryMapper;
//    }
    @Transactional
    public InventoryDto updateInventory(UpdateInventoryRequest req) {
        Item item = itemRepository.findById(req.itemId())
                .orElseThrow(() -> new IllegalArgumentException("Item " + req.itemId() + " not found"));


        Inventory inventory = inventoryRepository
                .findByItemForUpdate(item.getItemId())
                .orElseGet(() -> Inventory.builder()
                        .item(item)
                        .totalQuantity(0)
                        .reservedQuantity(0)
                        .lastUpdated(LocalDateTime.now())
                        .build());

        int newTotal = inventory.getTotalQuantity() + req.quantityToAdd();
        inventory.setTotalQuantity(newTotal);

        Inventory saved = inventoryRepository.save(inventory);

//        evictAvailabilityCache(item.getId());

        return inventoryMapper.toDto(saved);

//        private void evictAvailabilityCache(Long itemId) {
//            Cache cache = cacheManager.getCache("inventoryAvailable");
//            if (cache != null) {
//                cache.evict("inventory:available:%d".formatted(itemId));
//            }
//        }
    }

    @Transactional
    public int reserveStock(Long itemId, int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        Inventory inventory = inventoryRepository
                .findByItemForUpdate(itemId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Inventory not found for item " + itemId));

        int available = inventory.getAvailableQuantity();
        if (available < quantity) {
            throw new OutOfStockException(
                    "Only %d units available, requested %d".formatted(available, quantity));
        }

        inventory.setReservedQuantity(inventory.getReservedQuantity() + quantity);
        inventoryRepository.save(inventory);

        return inventory.getAvailableQuantity();

//        Item item = itemRepository.findById(req.itemId())
//                .orElseThrow(() -> new IllegalArgumentException("Item %d not found".formatted(req.itemId())));

//        Reservation reservation = Reservation.builder()
//                .item(item)
//                .quantity(req.quantity())
//                .status(Reservation.ReservationStatus.RESERVED)
//                .build();
//        reservationRepository.save(reservation);
//
//        return new ReserveInventoryResponse(
//                reservation.getReservationId(),
//                item.getItemId(),
//                req.quantity(),
//                reservation.getStatus(),
//                inventory.getAvailableQuantity()
//        );
    }

}
