package com.example.assessment.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inventoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    private int totalQuantity;

    private int reservedQuantity;

    private LocalDateTime lastUpdated;

    public int getAvailableQuantity() {
        return totalQuantity - reservedQuantity;
    }

    @PrePersist
    @PreUpdate
    private void touch() {
        this.lastUpdated = LocalDateTime.now();
    }
}
