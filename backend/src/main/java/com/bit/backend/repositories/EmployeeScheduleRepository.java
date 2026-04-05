package com.bit.backend.repositories;

import com.bit.backend.entities.EmployeeScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeScheduleRepository extends JpaRepository<EmployeeScheduleEntity, Long> {
}
