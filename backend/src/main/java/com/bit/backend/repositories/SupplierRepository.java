package com.bit.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bit.backend.entities.SupplierEntity;

public interface SupplierRepository extends JpaRepository<SupplierEntity, Long> {

}
