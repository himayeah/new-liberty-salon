package com.bit.backend.services;

import java.util.List;

import com.bit.backend.dtos.ProductDto;

public interface ReportProductSalesServiceI {

    List<ProductDto> getProductSales();

}
