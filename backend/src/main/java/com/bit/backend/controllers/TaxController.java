package com.bit.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bit.backend.dtos.TaxDto;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.services.TaxServiceI;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tax")
public class TaxController {

    private final TaxServiceI taxServiceI;

    public TaxController(TaxServiceI taxServiceI) {
        this.taxServiceI = taxServiceI;
    }

    @PostMapping
    public ResponseEntity<TaxDto> addForm(@RequestBody TaxDto taxDto) throws AppException {
        try {
            TaxDto taxDtoResponse = taxServiceI.addTax(taxDto);
            return ResponseEntity.created(URI.create("/tax/" + taxDtoResponse.getTaxName())).body(taxDtoResponse);

        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<TaxDto>> getData() {
        try {
            List<TaxDto> taxDtoList = taxServiceI.getData();
            return ResponseEntity.ok(taxDtoList);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<TaxDto> updateTax(
            @PathVariable long id,
            @RequestBody TaxDto taxDto) {
        try {
            TaxDto responseTaxDto = taxServiceI.updateTax(id, taxDto);
            return ResponseEntity.ok(responseTaxDto);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("{id")
    public ResponseEntity<TaxDto> deleteTax(@PathVariable long id) {
        try {
            TaxDto taxDto = taxServiceI.deleteTax(id);
            return ResponseEntity.ok(taxDto);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
