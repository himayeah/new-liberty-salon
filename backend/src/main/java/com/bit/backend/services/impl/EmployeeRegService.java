package com.bit.backend.services.impl;

import com.bit.backend.dtos.EmployeeRegDto;
import com.bit.backend.entities.EmployeeRegEntity;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.mappers.EmployeeRegMapper;
import com.bit.backend.repositories.EmployeeRegRepository;
import com.bit.backend.services.EmployeeRegServiceI;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeRegService implements EmployeeRegServiceI {

    private final EmployeeRegRepository employeeRegRepository;
    private final EmployeeRegMapper employeeRegMapper;

    public EmployeeRegService(EmployeeRegRepository employeeRegRepository, EmployeeRegMapper employeeRegMapper) {
        this.employeeRegRepository = employeeRegRepository;
        this.employeeRegMapper = employeeRegMapper;
    }

    @Override
    public EmployeeRegDto addEmployee(EmployeeRegDto employeeRegDto) {
        try{
            EmployeeRegEntity employeeRegEntity = employeeRegMapper.toEmployeeRegEntity(employeeRegDto);
            EmployeeRegEntity savedItem = employeeRegRepository.save(employeeRegEntity);
            EmployeeRegDto savedDto = employeeRegMapper.toEmployeeRegDto(savedItem);
            return savedDto;
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<EmployeeRegDto> getData() {
        try{
            List<EmployeeRegEntity> employeeRegEntityList = employeeRegRepository.findAll();
            List<EmployeeRegDto> employeeRegDtoList = employeeRegMapper.toEmployeeRegDtoList(employeeRegEntityList);
            return employeeRegDtoList;
        } catch (Exception e){
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public EmployeeRegDto updateEmployeeReg(long id, EmployeeRegDto employeeRegDto) {
        try{
            Optional<EmployeeRegEntity> optionalEmployeeRegEntity = employeeRegRepository.findById(id);
            if (!optionalEmployeeRegEntity.isPresent()){
                throw new AppException("Employee Reg Does Not Exist", HttpStatus.BAD_REQUEST);
            }
            EmployeeRegEntity newEmployeeRegEntity = employeeRegMapper.toEmployeeRegEntity(employeeRegDto);
            newEmployeeRegEntity.setId(id);
            EmployeeRegEntity employeeRegEntity = employeeRegRepository.save(newEmployeeRegEntity);
            EmployeeRegDto employeeRegDtoRes = employeeRegMapper.toEmployeeRegDto(employeeRegEntity);
            //System.out.println("update Successfully: " + employeeRegDtoRes.getFirstName());
            return employeeRegDtoRes;
        } catch (Exception e) {
            throw new AppException("Request filled with error:" + e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public EmployeeRegDto deleteEmployeeReg(long id) {
        try{
            Optional<EmployeeRegEntity> optionalEmployeeRegEntity = employeeRegRepository.findById(id);
            if (!optionalEmployeeRegEntity.isPresent()){
                throw new AppException("Employee Reg Does Not Exist", HttpStatus.BAD_REQUEST);
            }
            employeeRegRepository.deleteById(id);
            return employeeRegMapper.toEmployeeRegDto(optionalEmployeeRegEntity.get());
        } catch (Exception e) {
            throw new AppException("Request filled with error:" + e, HttpStatus.BAD_REQUEST);
            }
    }
}
