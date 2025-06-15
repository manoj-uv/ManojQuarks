package com.example.assessment.controller;

import com.example.assessment.dto.CancelReservationResponse;
import com.example.assessment.dto.ReserveInventoryRequest;
import com.example.assessment.dto.ReserveInventoryResponse;
import com.example.assessment.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
@Tag(
        name        = "Reservation",
        description = "Create, cancel, and manage item reservations"
)
public class ReservationController {

    private final ReservationService reservationService;

    @Operation(summary = "Reserve stock for an order")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock reserved",
                    content = @Content(schema =
                    @Schema(implementation = ReserveInventoryResponse.class))),
            @ApiResponse(responseCode = "409", description = "Not enough stock")
    })
    @PostMapping("/reserve")
    public ResponseEntity<ReserveInventoryResponse> create(@Valid @RequestBody ReserveInventoryRequest req) {
        return ResponseEntity.ok(reservationService.reserve(req));
    }

    @Operation(summary = "Cancel an existing reservation")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reservation cancelled",
                    content = @Content(schema =
                    @Schema(implementation = CancelReservationResponse.class))),
            @ApiResponse(responseCode = "404", description = "Reservation not found")
    })
    @PostMapping("/{id}/cancel")
    public ResponseEntity<CancelReservationResponse> cancel(@PathVariable("id") Long reservationId) {
        return ResponseEntity.ok(reservationService.cancelReservation(reservationId));
    }
}
