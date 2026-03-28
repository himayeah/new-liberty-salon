package com.bit.backend.services;

import com.bit.backend.dtos.EmployeeLeaveDto;

import java.util.List;

public interface EmployeeLeaveServiceI {
    EmployeeLeaveDto addEmployeeLeave(EmployeeLeaveDto employeeLeaveDto);
    List<EmployeeLeaveDto> getData();
    EmployeeLeaveDto updateEmployeeLeave(long id, EmployeeLeaveDto employeeLeaveDto);
    EmployeeLeaveDto deleteEmployeeLeave(long id);
}
