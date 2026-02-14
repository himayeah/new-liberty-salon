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
    private String brand;
    private String productDescription;
    private String Unit;
    private double purchasePrice;
    private double sellingPrice;
    private String barcode;
    private String sku;
    private boolean isTaxable;
    private int reOrderLevel;

    public String getProductName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
