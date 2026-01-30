package com.bit.backend.mappers;

import com.bit.backend.dtos.EmployeeLeaveDto;
import com.bit.backend.entities.EmployeeLeaveEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeLeaveMapper {
    EmployeeLeaveDto toEmployeeLeaveDto(EmployeeLeaveEntity employeeLeaveEntity);
    EmployeeLeaveEntity toEmployeeLeaveEntity(EmployeeLeaveDto employeeLeaveDto);
    List<EmployeeLeaveDto> toEmployeeLeaveDtoList(List<EmployeeLeaveEntity> employeeLeaveEntityList);
}
