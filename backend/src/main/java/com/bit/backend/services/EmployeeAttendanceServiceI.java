package com.bit.backend.services;

import com.bit.backend.dtos.EmployeeAttendanceDto;
import java.util.List;

public interface EmployeeAttendanceServiceI {
    EmployeeAttendanceDto addData(EmployeeAttendanceDto dto);
    List<EmployeeAttendanceDto> getData();
    EmployeeAttendanceDto getById(Long id);
    EmployeeAttendanceDto updateData(Long id, EmployeeAttendanceDto dto);
    EmployeeAttendanceDto deleteData(Long id);
}
