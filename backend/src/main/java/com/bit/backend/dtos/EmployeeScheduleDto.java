package com.bit.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeScheduleDto {
    private Long id;
    private Long employeeId;
    private Integer workDay;
    private Boolean isActive;
    private String startTime;
    private String endTime;
    private String effectiveDate;
    private String endDate;
}
