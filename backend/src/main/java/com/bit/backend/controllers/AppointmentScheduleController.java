package com.bit.backend.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bit.backend.dtos.AppointmentScheduleDto;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.services.AppointmentScheduleServiceI;

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
    public ResponseEntity<List<AppointmentScheduleDto>> getAppointments() {
        try {
            List<AppointmentScheduleDto> appointmentScheduleDtoList = appointmentScheduleServiceI.getAppointments();

            // print walk-in clients
            appointmentScheduleDtoList.stream()
                    .filter(dto -> "WALK_IN".equalsIgnoreCase(dto.getBookingSource()))
                    .forEach(dto -> System.out.println("WALK_IN Client: " + dto.getClientName()));

            // print all appointments
            appointmentScheduleDtoList.forEach(System.out::println);

            // convert into string so i can get a readable output in the terminal
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

    // Dashboard card (Get most used service name)
    @GetMapping("/appointment-schedule-form/get-most-used-service")
    public ResponseEntity<String> getMostUsedService() {
        try {
            String result = appointmentScheduleServiceI.getMostUsedService();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Dashboard chart (Get appointment counts by month for the last 6 months)
    @GetMapping("/appointment-schedule-form/count-by-month")
    public ResponseEntity<List<Object[]>> getAppointmentCountsByMonth() {
        try {
            List<Object[]> result = appointmentScheduleServiceI.getAppointmentCountsByMonth();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
