package com.bit.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AppointmentScheduleDto {

    private Long id;
    private Long clientId;
    private String clientName;
    private String clientPhone;
    private String clientEmail;
    private Long employeeId;
    private String employeeName;
    private String employeeEmail;
    private Long serviceId;
    private String serviceName;
    private String appointmentDate;
    private String appointmentStartTime;
    private String appointmentEndTime;
    private String appointmentStatus;
    private String bookingSource;
    private String notes;
    private String cancellationReason;
    private String cancelledDate;
    private String createdDate;
    private String createdBy;
    private boolean reminderSent;
    private boolean dashboardNotified;

    // Report DTOs
    private Long totalCount;

    // NEWLYADDED
    private String topEmployeeName;
    private Long topTotalAppointments;

}
