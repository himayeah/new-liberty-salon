package com.bit.backend.repositories;

import com.bit.backend.entities.ProductEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    // Product sales Report
    @Query(value = "SELECT p.product_name, SUM(g.received_quantity) AS total " +
            "FROM grn g " +
            "JOIN product p ON p.id = g.product_id " +
            "WHERE g.received_date BETWEEN '2026-01-01' AND '2026-03-31' " +
            "AND p.product_name = 'Shampoo(1L)- Dreamron' " +
            "GROUP BY p.product_name", nativeQuery = true)
    List<Object[]> productSales();

}
