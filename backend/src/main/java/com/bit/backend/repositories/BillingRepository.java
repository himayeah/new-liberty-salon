package com.bit.backend.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bit.backend.entities.BillingEntity;

public interface BillingRepository extends JpaRepository<BillingEntity, Long> {

        @Query(value = "SELECT p.product_name AS product_name, " +
            "COUNT(bp.product_id) AS sold_quantity, " +
            "SUM((p.selling_price - p.purchase_price) * bp.quantity) AS revenue " +
            "FROM billing_purchase bp " +
            "JOIN product p ON bp.product_id = p.id " +
            "GROUP BY bp.product_id, p.product_name", nativeQuery = true)
        List<Object[]> getProductSalesData();

}
