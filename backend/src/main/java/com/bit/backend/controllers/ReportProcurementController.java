package com.bit.backend.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.bit.backend.dtos.ReportProcurementDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bit.backend.services.ReportProcurementService;

import com.bit.backend.exceptions.AppException;

@RestController
@RequestMapping("/report/procurement")
public class ReportProcurementController {

    private final ReportProcurementService reportProcurementService;

    public ReportProcurementController(ReportProcurementService reportProcurementService) {
        this.reportProcurementService = reportProcurementService;
    }

    // . Procurement Report- Pending orders worth >= 1000000
    @GetMapping("/pending-purchase-orders")
    // line 19 method is the controller's method. line 22 method is the one that
    // calls service class
    public ResponseEntity<List<ReportProcurementDto>> getPendingPurchaseOrders() {
        try {
            List<ReportProcurementDto> dtoList = reportProcurementService.getPendingPurchaseOrders();
            return ResponseEntity.ok(dtoList);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Product Sales by Supplier
    @GetMapping("/product-sales-by-supplier")
    // line 19 method is the controller's method. line 22 method is the one that
    // calls service class
    public ResponseEntity<List<ReportProcurementDto>> getProductSalesBySupplier() {
        try {
            List<ReportProcurementDto> dtoList = reportProcurementService.getProductSalesBySupplier();
            return ResponseEntity.ok(dtoList);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
