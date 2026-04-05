package com.bit.backend.mappers;

import com.bit.backend.dtos.EmployeeAttendanceDto;
import com.bit.backend.entities.EmployeeAttendanceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeAttendanceMapper {

    @Mapping(source = "employeeRegEntity.id", target = "employeeId")
    @Mapping(source = "employeeRegEntity.employeeName", target = "employeeName")
    EmployeeAttendanceDto toEmployeeAttendanceDto(EmployeeAttendanceEntity employeeAttendanceEntity);

    @Mapping(source = "employeeId", target = "employeeRegEntity.id")
    @Mapping(source = "employeeName", target = "employeeRegEntity.employeeName")
    EmployeeAttendanceEntity toEmployeeAttendanceEntity(EmployeeAttendanceDto employeeAttendanceDto);

    List<EmployeeAttendanceDto> toEmployeeAttendanceDtoList(List<EmployeeAttendanceEntity> employeeAttendanceEntityList);
}
