package com.bit.backend.controllers;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bit.backend.dtos.ReportProductSalesDto;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.services.ReportProductSalesService;

@RestController
@RequestMapping("/report-product-sales")
public class ReportProductSalesController {

    private ReportProductSalesService reportProductSalesService;

    public ReportProductSalesController(ReportProductSalesService reportProductSalesService) {
        this.reportProductSalesService = reportProductSalesService;
    }

    @GetMapping(value = "/revenue", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReportProductSalesDto>> getProductSalesData() {
        try {
            List<ReportProductSalesDto> reportProductSalesDtoList = reportProductSalesService.getProductSalesData();
            System.out.println("Data:" + reportProductSalesDtoList);
            return ResponseEntity.ok(reportProductSalesDtoList);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}
