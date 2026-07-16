package com.bit.backend.controllers;

import com.bit.backend.dtos.InvoiceDto;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.services.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/billing/{billingId}")
    public ResponseEntity<InvoiceDto> getInvoiceByBillingId(@PathVariable long billingId) {
        try {
            InvoiceDto invoiceDto = invoiceService.getInvoiceByBillingId(billingId);
            return ResponseEntity.ok(invoiceDto);
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException("Request failed with error: " + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<InvoiceDto>> getInvoices() {
        try {
            List<InvoiceDto> invoiceDtoList = invoiceService.getInvoices();
            return ResponseEntity.ok(invoiceDtoList);
        } catch (Exception e) {
            throw new AppException("Request failed with error: " + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
