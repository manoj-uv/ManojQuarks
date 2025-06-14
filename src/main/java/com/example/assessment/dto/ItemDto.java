package com.example.assessment.dto;

public record ItemDto(
        Long itemId,
        String name,
        String sku,
        String description
) {}
