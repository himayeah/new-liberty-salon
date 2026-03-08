package com.bit.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentScheduleDto {

    private Long id;
    private Long clientId;
    private Long employeeId;
    private Long serviceId;

    private String clientName;
    private String employeeName;
    private String serviceName;

    private String appointmentDate;
    private String appointmentStartTime;
    private String appointmentEndTime;
    private String appointmentStatus;
    private String bookingSource;
    private String notes;
    private String cancellationReason;
    private String createdDate;
    private String createdBy;
    private String cancelledDate;

    public String getFirstName() {
        return clientName != null ? clientName : "";
    }
}
