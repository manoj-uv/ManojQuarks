package com.example.assessment.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateInventoryRequest(
        Long itemId,
        @Schema(description = "Units to add to totalQuantity", example = "50")
        int quantityToAdd) {}
