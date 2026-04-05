package com.bit.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bit.backend.dtos.AppointmentScheduleDto;
import com.bit.backend.dtos.ClientRegDto;
import com.bit.backend.dtos.ReportCancelledAppointmentScheduleDto;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.services.ReportAppointmentStatusService;

@RestController
@RequestMapping("/report-appointment-status")
public class ReportAppointmentStatusController {

    @Autowired
    private ReportAppointmentStatusService reportAppointmentStatusService;

    // Get total number of cancelled appointments in a given date range
    @GetMapping("/count")
    public ResponseEntity<Long> countCancelledAppointments() {
        try {
            Long count = reportAppointmentStatusService.countCancelledAppointments();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Client Name | Service Name | Cancelled Date | Cancelled Reason of Cancelled
    // Appointments within last 3 Months
    @GetMapping("/details")
    public ResponseEntity<List<ReportCancelledAppointmentScheduleDto>> getData() {
        try {
            List<ReportCancelledAppointmentScheduleDto> reportCancelledAppointmentScheduleDtoList = reportAppointmentStatusService
                    .getCancelledAppointmentDetails();
            return ResponseEntity.ok(reportCancelledAppointmentScheduleDtoList);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
