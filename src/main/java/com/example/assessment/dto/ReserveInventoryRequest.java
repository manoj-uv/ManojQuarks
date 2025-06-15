package com.example.assessment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ReserveInventoryRequest(
        @Schema(description = "Item identifier", example = "42")
        @NotNull Long itemId,

        @Schema(description = "Units to reserve", example = "2")
        @Positive int  quantity
) {
}
