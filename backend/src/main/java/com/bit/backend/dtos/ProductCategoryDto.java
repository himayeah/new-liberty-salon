package com.bit.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryDto {
    private String productCategoryName;

    public String getProductCategoryName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
