package com.bit.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bit.backend.dtos.ReportAppointmentStatusDto;
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

    // appointment cancellation table
    @GetMapping("/cancellation-details")
    public ResponseEntity<List<ReportAppointmentStatusDto>> getCancelledAppointmentDetails() {
        try{
            List<ReportAppointmentStatusDto>  reportAppointmentStatusDto = reportAppointmentStatusService.getCancelledAppointmentDetails();
            return ResponseEntity.ok(reportAppointmentStatusDto);
        }
        catch(Exception e){
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Report- getBookingsBySource (Pie Chart)
    @GetMapping("/bookings-by-source")
    public ResponseEntity<List<ReportAppointmentStatusDto>> getBookingsBySource() {
        try {
            List<ReportAppointmentStatusDto> reportAppointmentStatusDtoList = reportAppointmentStatusService
                    .getAppointmentsBySource();
            return ResponseEntity.ok(reportAppointmentStatusDtoList);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Report- get AppointmentCountByStatus (bar chart)
    @GetMapping(value = "/count-by-status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReportAppointmentStatusDto>> getAppointmentCountByStatus() {
        try {
            List<ReportAppointmentStatusDto> reportAppointmentStatusDtoList = reportAppointmentStatusService.getAppointmentCountByStatus();
            System.out.println("Data:" + reportAppointmentStatusDtoList);
            return ResponseEntity.ok(reportAppointmentStatusDtoList);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
