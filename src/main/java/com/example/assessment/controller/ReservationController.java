package com.example.assessment.controller;

import com.example.assessment.dto.ReserveInventoryRequest;
import com.example.assessment.dto.ReserveInventoryResponse;
import com.example.assessment.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/reserve")
    public ResponseEntity<ReserveInventoryResponse> create(@Valid @RequestBody ReserveInventoryRequest req) {
        return ResponseEntity.ok(reservationService.reserve(req));
    }
}
