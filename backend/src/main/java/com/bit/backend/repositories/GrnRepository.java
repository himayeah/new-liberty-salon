package com.bit.backend.repositories;

import com.bit.backend.entities.GrnEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GrnRepository extends JpaRepository<GrnEntity, Long> {
    List<GrnEntity> findByPurchaseOrder_Id(Long purchaseOrderId);
}
