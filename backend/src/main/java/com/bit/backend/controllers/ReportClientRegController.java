package com.bit.backend.controllers;

import com.bit.backend.dtos.ClientRegDto;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.services.ReportClientRegService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
