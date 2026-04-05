package com.bit.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bit.backend.dtos.ProductDto;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.services.ReportProductSalesServiceI;

@RestController
@RequestMapping("/report-product-sales-controller")
public class ReportProductSalesController {

    @Autowired
    private ReportProductSalesServiceI productServiceI;

    // Product Sales report
    @GetMapping("/report-product-sales")
    public ResponseEntity<List<ProductDto>> getProductSales() {
        try {
            List<ProductDto> productDtoList = productServiceI.getProductSales();
            return ResponseEntity.ok(productDtoList);
        } catch (Exception e) {
            throw new AppException("Couldn't get Product Report Details:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
