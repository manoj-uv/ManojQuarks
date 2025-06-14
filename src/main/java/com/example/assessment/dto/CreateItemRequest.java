package com.example.assessment.dto;


import jakarta.validation.constraints.NotBlank;

public record CreateItemRequest(
        @NotBlank String name,
        @NotBlank String sku,
        String description
) {}
