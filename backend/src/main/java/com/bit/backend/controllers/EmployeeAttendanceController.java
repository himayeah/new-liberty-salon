package com.bit.backend.controllers;

import com.bit.backend.dtos.EmployeeAttendanceDto;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.services.EmployeeAttendanceServiceI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/employee-attendance")
public class EmployeeAttendanceController {

    private final EmployeeAttendanceServiceI employeeAttendanceServiceI;

    public EmployeeAttendanceController(EmployeeAttendanceServiceI employeeAttendanceServiceI) {
        this.employeeAttendanceServiceI = employeeAttendanceServiceI;
    }

    @PostMapping
    public ResponseEntity<EmployeeAttendanceDto> addData(@RequestBody EmployeeAttendanceDto dto) throws AppException {
        try {
            EmployeeAttendanceDto responseDto = employeeAttendanceServiceI.addData(dto);
            return ResponseEntity.created(URI.create("/employee-attendance/" + responseDto.getId()))
                    .body(responseDto);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<EmployeeAttendanceDto>> getData() {
        try {
            List<EmployeeAttendanceDto> dtoList = employeeAttendanceServiceI.getData();
            return ResponseEntity.ok(dtoList);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeAttendanceDto> getById(@PathVariable Long id) {
        try {
            EmployeeAttendanceDto dto = employeeAttendanceServiceI.getById(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeAttendanceDto> updateData(
            @PathVariable Long id,
            @RequestBody EmployeeAttendanceDto dto) {
        try {
            EmployeeAttendanceDto responseDto = employeeAttendanceServiceI.updateData(id, dto);
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EmployeeAttendanceDto> deleteData(@PathVariable Long id) {
        try {
            EmployeeAttendanceDto dto = employeeAttendanceServiceI.deleteData(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
