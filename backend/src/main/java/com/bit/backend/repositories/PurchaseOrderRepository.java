package com.bit.backend.repositories;

import com.bit.backend.entities.PurchaseOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrderEntity, Long> {

    @Query(value = "SELECT " +
            "po.order_number AS order_number, " +
            "s.supplier_name AS supplier, " +
            "po.order_date AS order_date, " +
            "po.expected_delivery_date AS expected_date, " +
            "DATEDIFF(CURRENT_DATE, po.expected_delivery_date ) AS late_days, " +
            "po.total_amount AS total_worth " +
            "FROM purchase_order po " +
            "JOIN supplier s ON s.id = po.supplier_id " +
            "WHERE po.total_amount >= 100000 " +
            "AND po.status = 'PENDING' ", nativeQuery = true)
    List<Object[]> getPendingPurchaseOrders();

    @Query(value = "SELECT " +
            "s.supplier_name AS supplier, " +
            "SUM(po.total_amount) AS total_worth " +
            "FROM purchase_order po " +
            "JOIN supplier s ON s.id = po.supplier_id " +
            "WHERE po.status = 'RECEIVED' " +
            "AND po.order_date >= CURRENT_DATE() - INTERVAL 3 MONTH " +
            "GROUP BY s.supplier_name", nativeQuery = true)
    List<Object[]> getProductSalesBySupplier();

}
