package com.example.assessment.dto;

import java.time.LocalDateTime;

public record InventoryDto(
        Long inventoryId,
        Long itemId,
        int totalQuantity,
        int reservedQuantity,
        LocalDateTime lastUpdated
) {}
