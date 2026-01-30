package com.bit.backend.services.impl;

import com.bit.backend.dtos.EmployeeLeaveDto;
import com.bit.backend.entities.EmployeeLeaveEntity;
import com.bit.backend.entities.EmployeeRegEntity;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.mappers.EmployeeLeaveMapper;
import com.bit.backend.repositories.EmployeeLeaveRepository;
import com.bit.backend.repositories.EmployeeRegRepository;
import com.bit.backend.services.EmployeeLeaveServiceI;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class EmployeeLeaveService implements EmployeeLeaveServiceI {

    private final EmployeeLeaveRepository employeeLeaveRepository;
    private final EmployeeLeaveMapper employeeLeaveMapper;

    public EmployeeLeaveService(EmployeeLeaveRepository employeeLeaveRepository, EmployeeLeaveMapper employeeLeaveMapper) {
        this.employeeLeaveRepository = employeeLeaveRepository;
        this.employeeLeaveMapper = employeeLeaveMapper;
    }

    @Override
    public EmployeeLeaveDto addEmployeeLeave(EmployeeLeaveDto employeeLeaveDto) {
        try {
            EmployeeLeaveEntity employeeLeaveEntity = employeeLeaveMapper.toEmployeeLeaveEntity(employeeLeaveDto);
            EmployeeLeaveEntity savedItem = employeeLeaveRepository.save(employeeLeaveEntity);
            EmployeeLeaveDto savedDto = employeeLeaveMapper.toEmployeeLeaveDto(savedItem);
            return savedDto;
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<EmployeeLeaveDto> getData() {
        try {
            List<EmployeeLeaveEntity> employeeLeaveEntityList = employeeLeaveRepository.findAll();
            List<EmployeeLeaveDto> employeeLeaveDtoList = employeeLeaveMapper.toEmployeeLeaveDtoList(employeeLeaveEntityList);
            return employeeLeaveDtoList;
        } catch (Exception e) {
            throw new AppException("Request failed with error" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public EmployeeLeaveDto updateEmployeeLeave(long id, EmployeeLeaveDto employeeLeaveDto) {
        try {
            Optional<EmployeeLeaveEntity> optionalEmployeeLeaveEntity = employeeLeaveRepository.findById(id);
            if (!optionalEmployeeLeaveEntity.isPresent()) {
                throw new AppException("Employee Leave Does Not Exist", HttpStatus.BAD_REQUEST);
        }
            EmployeeLeaveEntity newEmployeeLeaveEntity = employeeLeaveMapper.toEmployeeLeaveEntity(employeeLeaveDto);
            newEmployeeLeaveEntity.setId(id);
            EmployeeLeaveEntity employeeLeaveEntity = employeeLeaveRepository.save(newEmployeeLeaveEntity);
            EmployeeLeaveDto employeeLeaveDtoRes = employeeLeaveMapper.toEmployeeLeaveDto(employeeLeaveEntity);
            System.out.println("update Successfully: " + employeeLeaveDtoRes.getEmployeeName());
            return employeeLeaveDtoRes;
    } catch (Exception e) {
            throw new AppException("Request filled with error:" + e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public EmployeeLeaveDto deleteEmployeeLeave(long id) {
        try {
            Optional<EmployeeLeaveEntity> optionalEmployeeLeaveEntity = employeeLeaveRepository.findById(id);
            if (!optionalEmployeeLeaveEntity.isPresent()) {
                throw new AppException("Employee Leave Does Not Exist", HttpStatus.BAD_REQUEST);
            }
            employeeLeaveRepository.deleteById(id);
            return employeeLeaveMapper.toEmployeeLeaveDto(optionalEmployeeLeaveEntity.get());
        } catch (Exception e) {
            throw new AppException("Request filled with error:" + e, HttpStatus.BAD_REQUEST);

        }
    }
}
