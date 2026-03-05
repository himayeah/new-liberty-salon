package com.bit.backend.controllers;

import com.bit.backend.dtos.ProductCategoryDto;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.services.ProductCategoryServiceI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/product-category")
public class ProductCategoryController {
    private final ProductCategoryServiceI productCategoryServiceI;

    public ProductCategoryController(ProductCategoryServiceI productCategoryServiceI) {
        this.productCategoryServiceI = productCategoryServiceI;
    }

    @PostMapping
    public ResponseEntity<ProductCategoryDto> addForm(@RequestBody ProductCategoryDto productCategoryDto)
            throws AppException {
        try {
            ProductCategoryDto productCategoryDtoResponse = productCategoryServiceI
                    .addProductCategory(productCategoryDto);
            return ResponseEntity.created(URI.create("/product-category/" + productCategoryDtoResponse.getId()))
                    .body(productCategoryDtoResponse);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<ProductCategoryDto>> getData() {
        try {
            List<ProductCategoryDto> productCategoryDtoList = productCategoryServiceI.getData();
            return ResponseEntity.ok(productCategoryDtoList);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductCategoryDto> updateProductCategory(
            @PathVariable long id,
            @RequestBody ProductCategoryDto productCategoryDto) {
        try {
            ProductCategoryDto responseProductCategoryDto = productCategoryServiceI.updateProductCategory(id,
                    productCategoryDto);
            return ResponseEntity.ok(responseProductCategoryDto);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductCategoryDto> deleteProductCategory(@PathVariable long id) {
        try {
            ProductCategoryDto productCategoryDto = productCategoryServiceI.deleteProductCategory(id);
            return ResponseEntity.ok(productCategoryDto);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
