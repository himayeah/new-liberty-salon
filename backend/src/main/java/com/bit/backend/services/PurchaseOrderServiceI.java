package com.bit.backend.services;

import com.bit.backend.dtos.PurchaseOrderDto;
import java.util.List;

public interface PurchaseOrderServiceI {
    PurchaseOrderDto addPurchaseOrder(PurchaseOrderDto purchaseOrderDto);
    List<PurchaseOrderDto> getPurchaseOrders();
    PurchaseOrderDto updatePurchaseOrder(Long id, PurchaseOrderDto purchaseOrderDto);
    PurchaseOrderDto deletePurchaseOrder(Long id);
    PurchaseOrderDto getPurchaseOrderById(Long id);
}
