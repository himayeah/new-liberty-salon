package com.bit.backend.repositories;

import com.bit.backend.entities.InvoiceItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceItemRepository extends JpaRepository<InvoiceItemEntity, Long> {
}
