package com.bit.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportCancelledAppointmentScheduleDto {

    private String clientName;
    private String serviceName;
    private String cancelledDate;
    private String cancellationReason;

}
