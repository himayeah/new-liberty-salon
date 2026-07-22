package com.bit.backend.repositories;

import com.bit.backend.entities.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity, Long> {
    java.util.Optional<InventoryEntity> findByProductProductName(String productName);
    java.util.Optional<InventoryEntity> findByProductId(Long productId);

    // Fetch all inventory records where currentStock is at or below product's reOrderLevel
    @Query("SELECT i FROM InventoryEntity i JOIN i.product p WHERE i.currentStock <= p.reOrderLevel")
    List<InventoryEntity> findProductsAtOrBelowReorderLevel();
}
