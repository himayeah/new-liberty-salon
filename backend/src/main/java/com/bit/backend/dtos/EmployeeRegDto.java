package com.bit.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeRegDto {

    private Long id;
    private String employeeName;
    private String dateJoined;
    private String designation;
    private String specializations;
    private String hourlyRate;
    private String commissionRate;
    private String weeklyOffDays;
    private String maxAppointmentsPerDay;
    private String actions;

}
