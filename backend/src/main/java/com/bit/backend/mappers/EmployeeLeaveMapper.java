package com.bit.backend.mappers;

import com.bit.backend.dtos.EmployeeLeaveDto;
import com.bit.backend.entities.EmployeeLeaveEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeLeaveMapper {
    @Mapping(target = "employeeName", source = "employeeRegEntity.employeeName")
    EmployeeLeaveDto toEmployeeLeaveDto(EmployeeLeaveEntity employeeLeaveEntity);

    @Mapping(target = "employeeRegEntity", ignore = true)
    EmployeeLeaveEntity toEmployeeLeaveEntity(EmployeeLeaveDto employeeLeaveDto);

    List<EmployeeLeaveDto> toEmployeeLeaveDtoList(List<EmployeeLeaveEntity> employeeLeaveEntityList);
}
