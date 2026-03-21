package com.bit.backend.controllers;

import com.bit.backend.dtos.AppointmentScheduleDto;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.services.AppointmentScheduleServiceI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AppointmentScheduleController {

    private final AppointmentScheduleServiceI appointmentScheduleServiceI;

    public AppointmentScheduleController(AppointmentScheduleServiceI appointmentScheduleServiceI) {
        this.appointmentScheduleServiceI = appointmentScheduleServiceI;

    }

    @PostMapping("/appointment-schedule-form")
    public ResponseEntity<AppointmentScheduleDto> addAppointment(
            @RequestBody AppointmentScheduleDto appointmentScheduleDto) {
        try {
            AppointmentScheduleDto appointmentScheduleDtoResponse = appointmentScheduleServiceI
                    .addAppointment(appointmentScheduleDto);
            return ResponseEntity.ok(appointmentScheduleDtoResponse);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/appointment-schedule-form")
    public ResponseEntity<List<AppointmentScheduleDto>> getAppoinyment() {
        try {
            List<AppointmentScheduleDto> appointmentScheduleDtoList = appointmentScheduleServiceI.getAppointments();
            return ResponseEntity.ok(appointmentScheduleDtoList);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/appointment-schedule-form/{id}")
    public ResponseEntity<AppointmentScheduleDto> updateAppointment(
            @PathVariable long id,
            @RequestBody AppointmentScheduleDto appointmentScheduleDto) {
        try {
            AppointmentScheduleDto responseAppointmentScheduleDto = appointmentScheduleServiceI.updateAppointment(id,
                    appointmentScheduleDto);
            return ResponseEntity.ok(responseAppointmentScheduleDto);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/appointment-schedule-form/{id}")
    public ResponseEntity<AppointmentScheduleDto> deleteAppointment(@PathVariable long id) {
        try {
            AppointmentScheduleDto appointmentScheduleDto = appointmentScheduleServiceI.deleteAppointment(id);
            return ResponseEntity.ok(appointmentScheduleDto);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Dashboard card (Get last 30 Days appointments)
    @GetMapping("/appointment-schedule-form/count-last-30-days")
    public ResponseEntity<Long> countAppointmentsLast30Days() {
        try {
            long count = appointmentScheduleServiceI.countAppointmentsLast30Days();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
