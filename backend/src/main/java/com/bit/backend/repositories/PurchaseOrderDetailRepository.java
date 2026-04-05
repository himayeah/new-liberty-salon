package com.bit.backend.repositories;

import com.bit.backend.entities.PurchaseOrderDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PurchaseOrderDetailRepository extends JpaRepository<PurchaseOrderDetailEntity, Long> {
    List<PurchaseOrderDetailEntity> findByPurchaseOrder_Id(Long purchaseOrderId);
}
