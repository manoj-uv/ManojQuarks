package com.example.assessment.dto;

public record ItemAvailabilityResponse(
        Long itemId,
        int  availableQuantity
) {
}
