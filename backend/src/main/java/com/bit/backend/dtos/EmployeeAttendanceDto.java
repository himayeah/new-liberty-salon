package com.bit.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeAttendanceDto {
    private Long id;
    private Long employeeId;
    private String employeeName;
    private String checkInTime;
    private String checkOutTime;
    private String status;
}
