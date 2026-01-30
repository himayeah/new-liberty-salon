package com.bit.backend.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentScheduleDto {

    private long id;
    private String clientName;
    private String stylistName;
    private String serviceName;
    private String appointmentDate;
    private String appointmentStartTime;
    private String appointmentEndTime;
    private String appointmentStatus;
    private String bookingSource;
    private String notes;
    private String cancellationReason;

    public String getFirstName() {
        return "";
    }

    public AppointmentScheduleDto(long id, String clientName, String stylistName, String serviceName, String appointmentDate, String appointmentStartTime, String appointmentEndTime, String appointmentStatus, String bookingSource, String notes, String cancellationReason) {
        this.id = id;
        this.clientName = clientName;
        this.stylistName = stylistName;
        this.serviceName = serviceName;
        this.appointmentDate = appointmentDate;
        this.appointmentStartTime = appointmentStartTime;
        this.appointmentEndTime = appointmentEndTime;
        this.appointmentStatus = appointmentStatus;
        this.bookingSource = bookingSource;
        this.notes = notes;
        this.cancellationReason = cancellationReason;
    }
}
