package com.bit.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bit.backend.dtos.ClientRegDto;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.services.ReportClientRegService;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/report-client-controller")
public class ReportClientRegController {

    @Autowired
    private ReportClientRegService reportClientRegService;

    // Report- Get total number of registered clients in a given date range
    @GetMapping
    public ResponseEntity<List<ClientRegDto>> getData() {
        try {
            List<ClientRegDto> clientRegDtoList = reportClientRegService.getRegistrationsByYear();
            return ResponseEntity.ok(clientRegDtoList);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Report- Get Registration Data by Gender
    @GetMapping("/registration-data")
    public ResponseEntity<List<ClientRegDto>> getRegistrationData() {
        try {
            List<ClientRegDto> clientRegDtoList = reportClientRegService.getRegistrationsByGender();
            return ResponseEntity.ok(clientRegDtoList);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Report- Get Registration Data by Age Group
    // To fix the data receiveing as XML type issue, use (value =
    // "/registrations-by-age-group", produces =
    // MediaType.APPLICATION_JSON_VALUE)part
    // And import org.springframework.http.MediaType;
    @GetMapping(value = "/registrations-by-age-group", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ClientRegDto>> getRegistrationsByAgeGroup(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate) {
        try {
            List<ClientRegDto> clientRegDtoList = reportClientRegService.getRegistrationsByAgeGroup(startDate, endDate);
            return ResponseEntity.ok(clientRegDtoList);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
