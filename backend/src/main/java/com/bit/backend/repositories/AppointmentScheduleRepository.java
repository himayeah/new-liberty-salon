package com.bit.backend.repositories;

import com.bit.backend.entities.AppointmentScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AppointmentScheduleRepository extends JpaRepository<AppointmentScheduleEntity, Long> {

    @Query(value = "SELECT COUNT(*) FROM appointment_schedule WHERE created_date >= :sinceDate", nativeQuery = true)
    long countAppointmentsAfter(@Param("sinceDate") String sinceDate);
}
