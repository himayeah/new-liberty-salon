package com.bit.backend.dtos;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportAppointmentStatusDto {

    // booking source
    private String bookingSource;
    private long totalCount;

    // appointment count by status (Bar chart)
    private long appointmentCount;
    private String appointmentStatus;
    
    // cancelled appointments table
    private String clientName;
    private String serviceName;
    private String cancelledDate;
    private String cancellationReason;



}
