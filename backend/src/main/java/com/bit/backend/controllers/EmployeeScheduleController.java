package com.bit.backend.controllers;

import com.bit.backend.dtos.EmployeeScheduleDto;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.services.EmployeeScheduleServiceI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/employee-schedule")
public class EmployeeScheduleController {

    private final EmployeeScheduleServiceI scheduleService;

    public EmployeeScheduleController(EmployeeScheduleServiceI scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ResponseEntity<EmployeeScheduleDto> createSchedule(@RequestBody EmployeeScheduleDto dto) {
        try {
            EmployeeScheduleDto created = scheduleService.createSchedule(dto);
            return ResponseEntity.created(URI.create("/api/v1/employee-schedule/" + created.getId())).body(created);
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException("Failed to create schedule: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<EmployeeScheduleDto>> getAllSchedules() {
        try {
            List<EmployeeScheduleDto> schedules = scheduleService.getAllSchedules();
            return ResponseEntity.ok(schedules);
        } catch (Exception e) {
            throw new AppException("Failed to fetch schedules: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeScheduleDto> updateSchedule(@PathVariable Long id, @RequestBody EmployeeScheduleDto dto) {
        try {
            EmployeeScheduleDto updated = scheduleService.updateSchedule(id, dto);
            return ResponseEntity.ok(updated);
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException("Failed to update schedule: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        try {
            scheduleService.deleteSchedule(id);
            return ResponseEntity.ok().build();
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException("Failed to delete schedule: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
