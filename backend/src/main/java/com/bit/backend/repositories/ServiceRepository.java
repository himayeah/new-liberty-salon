package com.bit.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bit.backend.entities.ServiceEntity;

public interface ServiceRepository extends JpaRepository <ServiceEntity, Long> {
    java.util.Optional<ServiceEntity> findByServiceName(String serviceName);
    java.util.Optional<ServiceEntity> findByServiceNameIgnoreCase(String serviceName);
}
