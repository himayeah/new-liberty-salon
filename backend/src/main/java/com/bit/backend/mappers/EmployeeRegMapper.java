package com.bit.backend.mappers;

import com.bit.backend.dtos.EmployeeRegDto;
import com.bit.backend.entities.EmployeeRegEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeRegMapper {
    EmployeeRegDto toEmployeeRegDto(EmployeeRegEntity employeeRegEntity);
    EmployeeRegEntity toEmployeeRegEntity(EmployeeRegDto employeeRegDto);
    List <EmployeeRegDto> toEmployeeRegDtoList(List<EmployeeRegEntity> employeeRegEntityList);
}
