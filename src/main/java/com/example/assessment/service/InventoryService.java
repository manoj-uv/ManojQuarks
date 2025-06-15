package com.example.assessment.service;

import com.example.assessment.dto.InventoryDto;
import com.example.assessment.dto.ItemAvailabilityResponse;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ItemRepository itemRepository;
    private final InventoryMapper inventoryMapper;
    private final ReservationRepository reservationRepository;

    @CacheEvict(value = "inventoryAvailable", key = "#req.itemId()")
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

        return inventoryMapper.toDto(saved);

    }

    @CacheEvict(value = "inventoryAvailable", key = "#itemId")
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
    }

    @CacheEvict(value = "inventoryAvailable", key = "#itemId")
    @Transactional
    public int releaseStock(Long itemId, int quantity) {

        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");

        Inventory inventory = inventoryRepository
                .findByItemForUpdate(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Inventory not found for item " + itemId));

        if (inventory.getReservedQuantity() < quantity) {
            throw new IllegalStateException("Reserved quantity underflow for item " + itemId);
        }

        inventory.setReservedQuantity(inventory.getReservedQuantity() - quantity);
        inventoryRepository.save(inventory);

        return inventory.getAvailableQuantity();
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "inventoryAvailable", key = "#itemId")
    public ItemAvailabilityResponse getAvailableQuantity(Long itemId) {
        log.info("Cache MISS for itemId: {}", itemId);
        Inventory inventory = inventoryRepository.findByItemId(itemId)
                 .orElseThrow(() -> new EntityNotFoundException("Inventory not found for item " + itemId));

         return new ItemAvailabilityResponse(itemId,inventory.getAvailableQuantity());
    }
}
