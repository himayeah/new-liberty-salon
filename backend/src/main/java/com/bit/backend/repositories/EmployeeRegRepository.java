package com.bit.backend.repositories;

import com.bit.backend.entities.EmployeeRegEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRegRepository extends JpaRepository<EmployeeRegEntity, Long> {
    Optional<EmployeeRegEntity> findByEmployeeName(String employeeName);
    Optional<EmployeeRegEntity> findByEmployeeNameAndEmail(String employeeName, String email);
    Optional<EmployeeRegEntity> findByEmail(String email);
    List<EmployeeRegEntity> findAllByEmail(String email);
    Optional<EmployeeRegEntity> findByInviteToken(String token);
    Optional<EmployeeRegEntity> findByResetToken(String token);
}
