package com.bit.backend.repositories;

import com.bit.backend.entities.EmployeeAttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeAttendanceRepository extends JpaRepository<EmployeeAttendanceEntity, Long> {
}
