package com.bit.backend.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.bit.backend.dtos.BillingDto;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.services.BillingService;

//@RestController says spring that this class will handle HTTP responses (Spring-Annotations)
@RestController
// @RequestMapping defines the base URL
@RequestMapping("/billing")
public class BillingController {

    // A dependancy. Controller calls the service. Service has the business logic
    private BillingService billingService;

    // Spring Injects the service into the Controller
    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @PostMapping
    public ResponseEntity<BillingDto> addBilling(
            @RequestBody BillingDto billingDto) {
        try {
            BillingDto billingDtoResponse = billingService.addBilling(billingDto);
            return ResponseEntity.ok(billingDtoResponse);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<BillingDto>> getBilling() {
        try {
            List<BillingDto> billingDtoList = billingService.getBilling();
            return ResponseEntity.ok(billingDtoList);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BillingDto> deleteBilling(@PathVariable long id) {
        try {
            BillingDto billingDto = billingService.deleteBilling(id);
            return ResponseEntity.ok(billingDto);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
