package com.example.assessment.controller;

import com.example.assessment.dto.CreateItemRequest;
import com.example.assessment.dto.ItemDto;
import com.example.assessment.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ResponseEntity<ItemDto> createItem(@RequestBody @Valid CreateItemRequest request) {
        return ResponseEntity.ok(itemService.createItem(request));
    }
}
