package com.bit.backend.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmployeeLeaveDto {
    private long id;
    private String employeeName;
    private String leaveType;
    private String startDate;
    private String endDate;
    private String reason;

    public EmployeeLeaveDto(long id, String employeeName, String leaveType, String startDate, String endDate, String reason) {
        this.id = id;
        this.employeeName = employeeName;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
    }
}

