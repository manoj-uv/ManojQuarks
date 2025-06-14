package com.example.assessment.dto;

public record UpdateInventoryRequest(
        Long itemId,
        int quantityToAdd) {}
