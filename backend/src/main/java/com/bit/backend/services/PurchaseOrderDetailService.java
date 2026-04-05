package com.bit.backend.services;

import com.bit.backend.dtos.PurchaseOrderDetailDto;
import java.util.List;

public interface PurchaseOrderDetailService {
    List<PurchaseOrderDetailDto> getAllPurchaseOrderDetails();
    List<PurchaseOrderDetailDto> getByPurchaseOrderId(Long purchaseOrderId);
    PurchaseOrderDetailDto savePurchaseOrderDetail(PurchaseOrderDetailDto dto);
    PurchaseOrderDetailDto updatePurchaseOrderDetail(Long id, PurchaseOrderDetailDto dto);
    void deletePurchaseOrderDetail(Long id);
}
