package com.bit.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bit.backend.dtos.ReportAppointmentStatusDto;
import com.bit.backend.entities.AppointmentScheduleEntity;

public interface AppointmentScheduleRepository extends JpaRepository<AppointmentScheduleEntity, Long> {

        // Finds all appointments for the next day that have not been reminded for
        // (Client reminders 24hrs prior to appointment time)
        @Query(value = "SELECT * FROM appointment_schedule " +
                        "WHERE appointment_date = DATE_FORMAT(CURRENT_DATE + INTERVAL 1 DAY, '%Y-%m-%d') " +
                        "AND reminder_sent = false " +
                        "AND appointment_status != 'CANCELLED'", nativeQuery = true)
        List<AppointmentScheduleEntity> findAppointmentsForReminder();

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

        // Dashboard chart (getAppointmentCountsByMonth)
        @Query(value = "SELECT " +
                        "monthname(CAST(appointment_date AS DATE)) AS appointment_month, " +
                        "COUNT(id) AS appointment_count " +
                        "FROM appointment_schedule " +
                        "WHERE CAST(appointment_date AS DATE) >= current_date - INTERVAL 6 MONTH " +
                        "GROUP BY monthname(CAST(appointment_date AS DATE))", nativeQuery = true)
        List<Object[]> getAppointmentCountsByMonth();

        // Dashboard pie chart- Top 3 services
        @Query(value = "SELECT " +
                        "s.service_name, " +
                        "COUNT(a.id) AS total_count " +
                        "FROM appointment_schedule a " +
                        "JOIN service s ON s.id = a.service_id " +
                        "WHERE STR_TO_DATE(a.appointment_date, '%Y-%m-%d') >= CURRENT_DATE() - INTERVAL 30 DAY " +
                        "GROUP BY s.service_name " +
                        "ORDER BY total_count DESC " +
                        "LIMIT 3 ", nativeQuery = true)
        List<Object[]> getTop3Services();

        // Dashboard Top 5 employee table
        @Query(value = "SELECT " +
                        "e.employee_name, " +
                        "COUNT(a.id) AS total_count " +
                        "FROM appointment_schedule a " +
                        "JOIN employee_reg e ON e.id =  a.employee_id " +
                        "WHERE STR_TO_DATE(a.appointment_date, '%Y-%m-%d') >= CURRENT_DATE() - INTERVAL 30 DAY " +
                        "GROUP BY e.employee_name " +
                        "ORDER BY total_count DESC " +
                        "LIMIT 5 ", nativeQuery = true)
        List<Object[]> getTop5Employees();

        // Dashboard Appointment Timeline Notification (Next 30 minutes)
        @Query(value = "SELECT * FROM appointment_schedule " +
                        "WHERE appointment_date = CURRENT_DATE " +
                        "AND dashboard_notified = false " +
                        "AND appointment_status != 'CANCELLED' " +
                        "AND STR_TO_DATE(appointment_start_time, '%H:%i') <= TIME_FORMAT(CURRENT_TIME + INTERVAL 30 MINUTE, '%H:%i') "
                        +
                        "AND STR_TO_DATE(appointment_start_time, '%H:%i') >= TIME_FORMAT(CURRENT_TIME, '%H:%i')", nativeQuery = true)
        List<AppointmentScheduleEntity> findUpcomingNotifications();

        // Report- getBookingsBySource (Pie Chart)
        @Query(value = "SELECT booking_source AS bookingSource, COUNT(id) AS totalCount " +
                        "FROM appointment_schedule " +
                        "WHERE appointment_date >= CURRENT_DATE - INTERVAL 30 DAY " +
                        "GROUP BY booking_source", nativeQuery = true)
        List<ReportAppointmentStatusDto> getAppointmentsBySource();

        // Retrieve the Max ID, Increment that value from 10 and return the fina Result
        // - Q2
        @Query(value = "SELECT MAX(id) FROM appointment_schedule as max_id", nativeQuery = true)
        Long getMaxId();

        // Appointment count by status (Bar chart)
        @Query(value = "SELECT appointment_status as appointment_status, COUNT(id) as _count " +
                        "FROM appointment_schedule " +
                        "WHERE appointment_date >= CURRENT_DATE - INTERVAL 30 DAY " +
                        "AND appointment_status IN('BOOKED','COMPLETED','CANCELLED') " +
                        "GROUP BY appointment_status " +
                        "ORDER BY appointment_status", nativeQuery = true)
        // native query is directly executed against db
        List<Object[]> getAppointmentCountByStatus();

}
