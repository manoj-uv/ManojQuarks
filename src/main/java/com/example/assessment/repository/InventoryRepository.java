package com.example.assessment.repository;

import com.example.assessment.entity.Inventory;
import com.example.assessment.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static jakarta.persistence.LockModeType.PESSIMISTIC_WRITE;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    @Lock(PESSIMISTIC_WRITE)
    @Query("select i from Inventory i where i.item.id = :itemId")
    Optional<Inventory> findByItemForUpdate(@Param("itemId") Long itemId);

    Optional<Inventory> findByItem(Item item);
}