package com.bit.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bit.backend.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.bit.backend.dtos.SupplierDto;
import com.bit.backend.services.SupplierServiceI;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/supplier")
public class SupplierController {

    private final SupplierServiceI supplierServiceI;

    public SupplierController(SupplierServiceI supplierServiceI) {
        this.supplierServiceI = supplierServiceI;
    }

    @PostMapping
    public ResponseEntity<SupplierDto> addForm(@RequestBody SupplierDto supplierDto) throws AppException {

        try {
            SupplierDto supplierDtoResponse = supplierServiceI.addSupplier(supplierDto);
            return ResponseEntity.created(URI.create("/supplier/" + supplierDtoResponse.getSupplierName()))
                    .body(supplierDtoResponse);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<SupplierDto>> getData() {
        try {
            List<SupplierDto> supplierDtoList = supplierServiceI.getSupplier();
            return ResponseEntity.ok(supplierDtoList);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<SupplierDto> updateSupplier(
            @PathVariable long id,
            @RequestBody SupplierDto supplierDto) {
        try {
            SupplierDto supplierDtoResponse = supplierServiceI.updateSupplier(id, supplierDto);
            return ResponseEntity.ok(supplierDtoResponse);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<SupplierDto> deleteSupplier(@PathVariable long id) {
        try {
            SupplierDto supplierDto = supplierServiceI.deleteSupplier(id);
            return ResponseEntity.ok(supplierDto);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
