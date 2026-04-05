package com.bit.backend.services;

import com.bit.backend.dtos.EmployeeScheduleDto;
import java.util.List;

public interface EmployeeScheduleServiceI {
    EmployeeScheduleDto createSchedule(EmployeeScheduleDto dto);

    List<EmployeeScheduleDto> getAllSchedules();

    EmployeeScheduleDto updateSchedule(Long id, EmployeeScheduleDto dto);

    void deleteSchedule(Long id);
}
