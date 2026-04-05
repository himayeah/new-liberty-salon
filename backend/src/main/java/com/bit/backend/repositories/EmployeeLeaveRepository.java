package com.bit.backend.repositories;

import com.bit.backend.entities.EmployeeLeaveEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeLeaveRepository extends JpaRepository<EmployeeLeaveEntity, Long> {
}
