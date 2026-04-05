package com.bit.backend.services.impl;

import com.bit.backend.dtos.EmployeeScheduleDto;
import com.bit.backend.entities.EmployeeRegEntity;
import com.bit.backend.entities.EmployeeScheduleEntity;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.mappers.EmployeeScheduleMapper;
import com.bit.backend.repositories.EmployeeRegRepository;
import com.bit.backend.repositories.EmployeeScheduleRepository;
import com.bit.backend.services.EmployeeScheduleServiceI;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeScheduleServiceImpl implements EmployeeScheduleServiceI {

    private final EmployeeScheduleRepository scheduleRepository;
    private final EmployeeRegRepository employeeRegRepository;
    private final EmployeeScheduleMapper mapper;

    public EmployeeScheduleServiceImpl(EmployeeScheduleRepository scheduleRepository,
                                       EmployeeRegRepository employeeRegRepository,
                                       EmployeeScheduleMapper mapper) {
        this.scheduleRepository = scheduleRepository;
        this.employeeRegRepository = employeeRegRepository;
        this.mapper = mapper;
    }

    @Override
    public EmployeeScheduleDto createSchedule(EmployeeScheduleDto dto) {
        EmployeeRegEntity employee = employeeRegRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new AppException("Employee not found", HttpStatus.NOT_FOUND));

        EmployeeScheduleEntity entity = mapper.toEntity(dto);
        entity.setEmployeeRegEntity(employee);

        EmployeeScheduleEntity saved = scheduleRepository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    public List<EmployeeScheduleDto> getAllSchedules() {
        return scheduleRepository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeScheduleDto updateSchedule(Long id, EmployeeScheduleDto dto) {
        EmployeeScheduleEntity existing = scheduleRepository.findById(id)
                .orElseThrow(() -> new AppException("Schedule not found", HttpStatus.NOT_FOUND));

        if (dto.getEmployeeId() != null && 
            (existing.getEmployeeRegEntity() == null || 
            !existing.getEmployeeRegEntity().getId().equals(dto.getEmployeeId()))) {
            EmployeeRegEntity employee = employeeRegRepository.findById(dto.getEmployeeId())
                    .orElseThrow(() -> new AppException("Employee not found", HttpStatus.NOT_FOUND));
            existing.setEmployeeRegEntity(employee);
        }

        existing.setWorkDay(dto.getWorkDay());
        existing.setIsActive(dto.getIsActive());
        existing.setStartTime(dto.getStartTime());
        existing.setEndTime(dto.getEndTime());
        existing.setEffectiveDate(dto.getEffectiveDate());
        existing.setEndDate(dto.getEndDate());

        EmployeeScheduleEntity updated = scheduleRepository.save(existing);
        return mapper.toDto(updated);
    }

    @Override
    public void deleteSchedule(Long id) {
        if (!scheduleRepository.existsById(id)) {
            throw new AppException("Schedule not found", HttpStatus.NOT_FOUND);
        }
        scheduleRepository.deleteById(id);
    }
}
