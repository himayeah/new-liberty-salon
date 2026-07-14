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

    private String bookingSource;
    private long totalCount;
    private long appointmentCount;
    private String appointmentStatus;
      
}
