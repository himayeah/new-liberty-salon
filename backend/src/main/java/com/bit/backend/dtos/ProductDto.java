package com.bit.backend.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private String productName;
    private String categoryName;

    private String productDescription;
    private String unit;
    private double purchasePrice;
    private double sellingPrice;
    private String barcode;
    private String sku;
    private boolean isTaxable;
    private int reOrderLevel;

    // product sales report's setTotal should be defined in the DTO
    private long total;
}
