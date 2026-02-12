package com.bit.backend.services;

import com.bit.backend.dtos.ProductCategoryDto;

import java.util.List;

public interface ProductCategoryServiceI {
    ProductCategoryDto addProductCategory(ProductCategoryDto productCategoryDto);
    List<ProductCategoryDto> getData();
    ProductCategoryDto updateProductCategory(long id, ProductCategoryDto productCategoryDto);
    ProductCategoryDto deleteProductCategory(long id);
}
