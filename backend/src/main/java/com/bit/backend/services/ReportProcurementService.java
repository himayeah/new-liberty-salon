package com.bit.backend.services;

import com.bit.backend.dtos.ReportProcurementDto;

import java.util.List;

public interface ReportProcurementService {

    // Procurement Report- Pending orders worth >= 1000000
    List<ReportProcurementDto> getPendingPurchaseOrders();

    // Product Sales by Supplier
    List<ReportProcurementDto> getProductSalesBySupplier();

}
