package com.bit.backend.repositories;

import com.bit.backend.entities.AppointmentScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AppointmentScheduleRepository extends JpaRepository<AppointmentScheduleEntity, Long> {

    // Dashboard Card (Total Appointments in last 30 Days)
    @Query(value = "SELECT COUNT(*) FROM appointment_schedule WHERE created_date >= :sinceDate", nativeQuery = true)
    long countAppointmentsAfter(@Param("sinceDate") String sinceDate);

    // Dashboard Card (Mostly used service Name in Last 30 Days)
    @Query(value = "SELECT s.service_name, COUNT(*) AS total_appointments" +
            "FROM appointment_Schedule a" +
            "JOIN service s ON s.id = a.service_id WHERE a.created_date >= CURRENT_DATE - INTERVAL 30 DAY GROUP BY s.service_name"
            +
            "ORDER BY total_appointments" +
            "DESC LIMIT 1", nativeQuery = true)
    String getMostUsedService();
}
