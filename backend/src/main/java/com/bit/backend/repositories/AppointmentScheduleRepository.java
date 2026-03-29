package com.bit.backend.repositories;

import com.bit.backend.entities.AppointmentScheduleEntity;

import java.util.List;

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

        // Appointment Status Report- Cancelled appointments in a given date range
        @Query(value = "SELECT COUNT(*) " +
                        "FROM appointment_schedule " +
                        "WHERE appointment_status = 'CANCELLED' AND appointment_date BETWEEN '2026-01-01' AND '2026-03-31' ", nativeQuery = true)
        long countCancelledAppointments();

        // Client Name | Service Name | Cancelled Date | Cancelled Reason of Cancelled
        // Appointments within last 3 Months
        @Query(value = "SELECT " +
                        "CONCAT(c.first_name, ' ', c.last_name) AS client_name," +
                        "s.service_name AS service_name, " +
                        "a.cancelled_date AS cancelled_date, " +
                        "a.cancellation_reason AS cancellation_reason " +
                        "FROM appointment_schedule a " +
                        "JOIN client_registration c ON c.id = a.client_id " +
                        "JOIN service s ON s.id = a.service_id " +
                        "WHERE a.appointment_status = 'CANCELLED' " +
                        "AND a.cancelled_date >= CURRENT_DATE - INTERVAL 3 MONTH ", nativeQuery = true)
        List<Object[]> getCancelledAppointmentDetails();

}
