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
    private Long employeeId;
    private String employeeName;
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

}
