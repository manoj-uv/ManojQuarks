package com.example.assessment.controller;

import com.example.assessment.dto.InventoryDto;
import com.example.assessment.dto.ItemAvailabilityResponse;
import com.example.assessment.dto.UpdateInventoryRequest;
import com.example.assessment.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/inventory")
@Tag(name = "Inventory", description = "Supply & availability APIs")
public class InventoryController {

    private InventoryService inventoryService;
    public InventoryController(InventoryService obj){
        inventoryService = obj;
    }

    @Operation(summary = "Add or top‑up inventory")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Inventory updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InventoryDto.class))),
            @ApiResponse(responseCode = "404", description = "Item not found")
    })
    @PostMapping("/update")
    public ResponseEntity<InventoryDto> updateInventory(@RequestBody @Valid UpdateInventoryRequest inventoryRequest){
        InventoryDto updatedInventoryDto = inventoryService.updateInventory(inventoryRequest);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(updatedInventoryDto.inventoryId())
                .toUri();
        return ResponseEntity.created(location).body(updatedInventoryDto);
    }

    @Operation(summary = "Get current available quantity")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(schema =
                    @Schema(implementation = ItemAvailabilityResponse.class))),
            @ApiResponse(responseCode = "404", description = "Item/Inventory missing")
    })
    @GetMapping("/availability/{itemId}")
    public ResponseEntity<ItemAvailabilityResponse> getAvailability(@PathVariable Long itemId){
        return ResponseEntity.ok(inventoryService.getAvailableQuantity(itemId));
    }
}
