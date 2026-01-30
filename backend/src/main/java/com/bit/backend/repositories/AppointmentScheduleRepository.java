package com.bit.backend.repositories;

import com.bit.backend.entities.AppointmentScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentScheduleRepository extends JpaRepository<AppointmentScheduleEntity, Long> {
}
