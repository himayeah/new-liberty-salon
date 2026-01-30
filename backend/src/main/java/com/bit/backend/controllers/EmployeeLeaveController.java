package com.bit.backend.controllers;

import com.bit.backend.dtos.EmployeeLeaveDto;
import com.bit.backend.dtos.EmployeeLeaveDto;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.services.EmployeeLeaveServiceI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import java.net.URI;

@RestController
@RequestMapping ("/employee_leave")
public class EmployeeLeaveController {

    private final EmployeeLeaveServiceI employeeLeaveServiceI;

    public EmployeeLeaveController(EmployeeLeaveServiceI employeeLeaveServiceI) {
        this.employeeLeaveServiceI = employeeLeaveServiceI;
    }

    @PostMapping
    public ResponseEntity<EmployeeLeaveDto> addForm(@RequestBody EmployeeLeaveDto employeeLeaveDto) throws AppException {
        try{
            EmployeeLeaveDto employeeLeaveDtoResponse = employeeLeaveServiceI.addEmployeeLeave(employeeLeaveDto);
            return ResponseEntity.created(URI.create("/employee_leave")).body(employeeLeaveDtoResponse);
            } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/employee-leave-get")
    public ResponseEntity<List<EmployeeLeaveDto>> getData() {
        try{
            List<EmployeeLeaveDto> employeeLeaveDto = employeeLeaveServiceI.getData();
            return ResponseEntity.ok(employeeLeaveDto);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeLeaveDto> updateEmployeeLeave(
            @PathVariable long id,
            @RequestBody EmployeeLeaveDto employeeLeaveDto) {
        try {
            EmployeeLeaveDto responseEmployeeLeaveDto = employeeLeaveServiceI.updateEmployeeLeave(id, employeeLeaveDto);
            return ResponseEntity.ok(responseEmployeeLeaveDto);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EmployeeLeaveDto> deleteEmployeeLeave(@PathVariable long id) {
        try {
            EmployeeLeaveDto employeeRegDto = employeeLeaveServiceI.deleteEmployeeLeave(id);
            return ResponseEntity.ok(employeeRegDto);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
