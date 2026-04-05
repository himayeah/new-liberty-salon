package com.bit.backend.mappers;

import com.bit.backend.dtos.EmployeeScheduleDto;
import com.bit.backend.entities.EmployeeScheduleEntity;
import org.springframework.stereotype.Component;

@Component
public class EmployeeScheduleMapper {

    public EmployeeScheduleEntity toEntity(EmployeeScheduleDto dto) {
        if (dto == null) {
            return null;
        }

        EmployeeScheduleEntity entity = new EmployeeScheduleEntity();
        entity.setId(dto.getId());
        entity.setWorkDay(dto.getWorkDay());
        entity.setIsActive(dto.getIsActive());
        entity.setStartTime(dto.getStartTime());
        entity.setEndTime(dto.getEndTime());
        entity.setEffectiveDate(dto.getEffectiveDate());
        entity.setEndDate(dto.getEndDate());
        return entity;
    }

    public EmployeeScheduleDto toDto(EmployeeScheduleEntity entity) {
        if (entity == null) {
            return null;
        }

        EmployeeScheduleDto dto = new EmployeeScheduleDto();
        dto.setId(entity.getId());
        if (entity.getEmployeeRegEntity() != null) {
            dto.setEmployeeId(entity.getEmployeeRegEntity().getId());
        }
        dto.setWorkDay(entity.getWorkDay());
        dto.setIsActive(entity.getIsActive());
        dto.setStartTime(entity.getStartTime());
        dto.setEndTime(entity.getEndTime());
        dto.setEffectiveDate(entity.getEffectiveDate());
        dto.setEndDate(entity.getEndDate());
        return dto;
    }
}
