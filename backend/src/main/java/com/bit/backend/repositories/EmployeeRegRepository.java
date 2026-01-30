package com.bit.backend.repositories;

import com.bit.backend.entities.EmployeeRegEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRegRepository extends JpaRepository<EmployeeRegEntity, Long> {
}
