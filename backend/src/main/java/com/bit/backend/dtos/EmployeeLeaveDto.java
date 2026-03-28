package com.bit.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeLeaveDto {
    private Long id;
    private String employeeName;
    private String leaveType;
    private String startDate;
    private String endDate;
    private String reason;
}
