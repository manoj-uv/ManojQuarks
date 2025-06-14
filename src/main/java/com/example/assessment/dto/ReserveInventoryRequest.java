package com.example.assessment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ReserveInventoryRequest(
        @NotNull Long itemId,
        @Positive int  quantity
) {
}
