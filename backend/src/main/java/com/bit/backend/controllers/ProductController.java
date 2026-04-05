package com.bit.backend.controllers;

import com.bit.backend.dtos.ProductDto;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.services.ProductServiceI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductServiceI productServiceI;

    public ProductController(ProductServiceI productServiceI) {
        this.productServiceI = productServiceI;
    }

    @PostMapping
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto productDto) throws AppException {
        try {
            ProductDto productDtoResponse = productServiceI.addProduct(productDto);
            return ResponseEntity.created(URI.create("/product/" + productDtoResponse.getId()))
                    .body(productDtoResponse);
        } catch (Exception e) {
            throw new AppException("Request failed with error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getData() {
        try {
            List<ProductDto> productDtoList = productServiceI.getData();
            return ResponseEntity.ok(productDtoList);
        } catch (Exception e) {
            throw new AppException("Request failed with error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable long id, @RequestBody ProductDto productDto) {
        try {
            ProductDto responseProductDto = productServiceI.updateProduct(id, productDto);
            return ResponseEntity.ok(responseProductDto);
        } catch (Exception e) {
            throw new AppException("Request failed with error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDto> deleteProduct(@PathVariable long id) {
        try {
            ProductDto responseProductDto = productServiceI.deleteProduct(id);
            return ResponseEntity.ok(responseProductDto);
        } catch (Exception e) {
            throw new AppException("Request failed with error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
