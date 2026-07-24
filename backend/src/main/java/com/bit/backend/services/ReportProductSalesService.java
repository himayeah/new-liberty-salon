package com.bit.backend.services;
import java.util.List;

import com.bit.backend.dtos.ReportProductSalesDto;

public interface ReportProductSalesService {

     List<ReportProductSalesDto> getProductSalesData();
    
}
