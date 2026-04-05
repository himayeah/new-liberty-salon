package com.bit.backend.repositories;

import com.bit.backend.entities.EmployeeRegEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRegRepository extends JpaRepository<EmployeeRegEntity, Long> {
    Optional<EmployeeRegEntity> findByEmployeeName(String employeeName);
}
