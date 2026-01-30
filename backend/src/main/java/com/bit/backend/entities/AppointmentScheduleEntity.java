package com.bit.backend.entities;

import jakarta.persistence.*;

@Entity
@Table(name= "appointment_schedule")
public class AppointmentScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="client_name")
    private String clientName;

    @Column(name="stylist_name")
    private String stylistName;

    @Column(name="service_name")
    private String serviceName;

    @Column(name="appointment_date")
    private String AppointmentDate;

    @Column(name="appointment_start_time")
    private String AppointmentStartTime;

    @Column(name="appointment_end_time")
    private String AppointmentEndTime;

    @Column(name="appointment_status")
    private String appointmentStatus;

    @Column(name="booking_source")
    private String bookingSource;

    @Column(name="notes")
    private String notes;

    @Column(name="cancellation_reason")
    private String cancellationReason;

    public AppointmentScheduleEntity(Long id, String clientName, String stylistName, String serviceName, String appointmentDate, String appointmentStartTime, String appointmentEndTime, String appointmentStatus, String bookingSource, String notes, String cancellationReason) {
        this.id = id;
        this.clientName = clientName;
        this.stylistName = stylistName;
        this.serviceName = serviceName;
        AppointmentDate = appointmentDate;
        AppointmentStartTime = appointmentStartTime;
        AppointmentEndTime = appointmentEndTime;
        this.appointmentStatus = appointmentStatus;
        this.bookingSource = bookingSource;
        this.notes = notes;
        this.cancellationReason = cancellationReason;
    }

    public void setId(long id) {

    }
}
