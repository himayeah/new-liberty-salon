package com.bit.backend.repositories;

import com.bit.backend.entities.ServiceEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ServiceRepository extends JpaRepository <ServiceEntity, Long> {
}
