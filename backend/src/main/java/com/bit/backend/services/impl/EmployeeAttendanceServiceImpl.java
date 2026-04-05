package com.bit.backend.services.impl;

import com.bit.backend.dtos.EmployeeAttendanceDto;
import com.bit.backend.entities.EmployeeAttendanceEntity;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.mappers.EmployeeAttendanceMapper;
import com.bit.backend.repositories.EmployeeAttendanceRepository;
import com.bit.backend.services.EmployeeAttendanceServiceI;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeAttendanceServiceImpl implements EmployeeAttendanceServiceI {

    private final EmployeeAttendanceRepository employeeAttendanceRepository;
    private final EmployeeAttendanceMapper employeeAttendanceMapper;

    public EmployeeAttendanceServiceImpl(EmployeeAttendanceRepository employeeAttendanceRepository, EmployeeAttendanceMapper employeeAttendanceMapper) {
        this.employeeAttendanceRepository = employeeAttendanceRepository;
        this.employeeAttendanceMapper = employeeAttendanceMapper;
    }

    @Override
    public EmployeeAttendanceDto addData(EmployeeAttendanceDto dto) {
        try {
            EmployeeAttendanceEntity entity = employeeAttendanceMapper.toEmployeeAttendanceEntity(dto);
            EmployeeAttendanceEntity savedItem = employeeAttendanceRepository.save(entity);
            return employeeAttendanceMapper.toEmployeeAttendanceDto(savedItem);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<EmployeeAttendanceDto> getData() {
        try {
            List<EmployeeAttendanceEntity> entities = employeeAttendanceRepository.findAll();
            return employeeAttendanceMapper.toEmployeeAttendanceDtoList(entities);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public EmployeeAttendanceDto updateData(Long id, EmployeeAttendanceDto dto) {
        try {
            Optional<EmployeeAttendanceEntity> optionalEntity = employeeAttendanceRepository.findById(id);
            if (!optionalEntity.isPresent()) {
                throw new AppException("Employee Attendance Does Not Exist", HttpStatus.BAD_REQUEST);
            }
            EmployeeAttendanceEntity newEntity = employeeAttendanceMapper.toEmployeeAttendanceEntity(dto);
            newEntity.setId(id);
            EmployeeAttendanceEntity savedEntity = employeeAttendanceRepository.save(newEntity);
            return employeeAttendanceMapper.toEmployeeAttendanceDto(savedEntity);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public EmployeeAttendanceDto deleteData(Long id) {
        try {
            Optional<EmployeeAttendanceEntity> optionalEntity = employeeAttendanceRepository.findById(id);
            if (!optionalEntity.isPresent()) {
                throw new AppException("Employee Attendance Does Not Exist", HttpStatus.BAD_REQUEST);
            }
            employeeAttendanceRepository.deleteById(id);
            return employeeAttendanceMapper.toEmployeeAttendanceDto(optionalEntity.get());
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public EmployeeAttendanceDto getById(Long id) {
        try {
            Optional<EmployeeAttendanceEntity> optionalEntity = employeeAttendanceRepository.findById(id);
            if (!optionalEntity.isPresent()) {
                throw new AppException("Employee Attendance Does Not Exist", HttpStatus.BAD_REQUEST);
            }
            return employeeAttendanceMapper.toEmployeeAttendanceDto(optionalEntity.get());
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
