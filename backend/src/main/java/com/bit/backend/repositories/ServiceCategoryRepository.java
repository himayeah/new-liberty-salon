package com.bit.backend.repositories;

import com.bit.backend.entities.ServiceCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceCategoryRepository extends JpaRepository <ServiceCategoryEntity, Long> {
}
