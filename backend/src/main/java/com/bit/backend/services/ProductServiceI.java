package com.bit.backend.services;

import com.bit.backend.dtos.ProductDto;
import java.util.List;

public interface ProductServiceI {

    ProductDto addProduct(ProductDto productDto);

    List<ProductDto> getData();

    ProductDto updateProduct(long id, ProductDto productDto);

    ProductDto deleteProduct(long id);

    // product sales report
    // <Object[]> getProductSales(); -- Don't write object[] as a return type. It's
    // a raw database result. Keep as it is (Object[]) in the Repo. it's fine.
    // fix the service method as below
    List<ProductDto> getProductSales();

}
