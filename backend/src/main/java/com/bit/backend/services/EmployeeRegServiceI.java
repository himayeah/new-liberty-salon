package com.bit.backend.services;

import com.bit.backend.dtos.EmployeeRegDto;

import java.util.List;

public interface EmployeeRegServiceI {
    EmployeeRegDto addEmployee(EmployeeRegDto employeeRegDto);
    List<EmployeeRegDto> getData();
    EmployeeRegDto updateEmployeeReg(long id, EmployeeRegDto employeeRegDto);
    EmployeeRegDto deleteEmployeeReg(long id);
}
