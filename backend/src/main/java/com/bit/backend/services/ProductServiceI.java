package com.bit.backend.services;

import com.bit.backend.dtos.ProductDto;
import java.util.List;

public interface ProductServiceI {

    ProductDto addProduct(ProductDto productDto);

    List<ProductDto> getData();

    ProductDto updateProduct(long id, ProductDto productDto);

    ProductDto deleteProduct(long id);

    // product sales report
    List<Object[]> productSales();

}
