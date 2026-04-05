package com.bit.backend.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bit.backend.dtos.ProductDto;
import com.bit.backend.repositories.ProductRepository;
import com.bit.backend.services.ReportProductSalesServiceI;

@Service
public class ReportProductSalesService implements ReportProductSalesServiceI {

    @Autowired
    private ProductRepository productRepository;

    // Product sales Report. This returns a result of 2 rows. So it should be a List
    // type Return
    @Override
    public List<ProductDto> getProductSales() {

        List<Object[]> results = productRepository.getProductSales();

        List<ProductDto> list = new ArrayList<>();

        for (Object[] row : results) {
            ProductDto dto = new ProductDto();

            dto.setProductName((String) row[0]);
            dto.setTotal(((Number) row[1]).longValue());

            list.add(dto);
        }

        return list;
    }

}
