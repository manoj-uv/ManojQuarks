package com.example.assessment.controller;

import com.example.assessment.dto.InventoryDto;
import com.example.assessment.dto.UpdateInventoryRequest;
import com.example.assessment.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private InventoryService inventoryService;
    public InventoryController(InventoryService obj){
        inventoryService = obj;
    }

    @PostMapping("/update")
    public ResponseEntity<InventoryDto> updateInventory(@RequestBody UpdateInventoryRequest inventoryRequest){
        InventoryDto updatedInventoryDto = inventoryService.updateInventory(inventoryRequest);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(updatedInventoryDto.inventoryId())
                .toUri();
        return ResponseEntity.created(location).body(updatedInventoryDto);
    }
}
