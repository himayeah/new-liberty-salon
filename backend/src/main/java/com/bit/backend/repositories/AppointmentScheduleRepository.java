package com.bit.backend.repositories;

import com.bit.backend.entities.AppointmentScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AppointmentScheduleRepository extends JpaRepository<AppointmentScheduleEntity, Long> {

    // Dashboard Card (Total Appointments in last 30 Days)
    @Query(value = "SELECT COUNT(*) FROM appointment_schedule WHERE STR_TO_DATE(appointment_date, '%Y-%m-%d') BETWEEN CURRENT_DATE - INTERVAL 30 DAY AND CURRENT_DATE", nativeQuery = true)
    long countAppointmentsLast30Days();

    @Query(value = "SELECT s.service_name " +
            "FROM appointment_schedule a " +
            "JOIN service s ON s.id = a.service_id WHERE STR_TO_DATE(a.appointment_date, '%Y-%m-%d') BETWEEN CURRENT_DATE - INTERVAL 30 DAY AND CURRENT_DATE "
            +
            "GROUP BY s.service_name " +
            "ORDER BY COUNT(*) DESC LIMIT 1", nativeQuery = true)
    String getMostUsedService();

}
