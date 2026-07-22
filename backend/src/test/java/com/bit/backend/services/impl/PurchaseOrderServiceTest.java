package com.bit.backend.services.impl;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyList;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bit.backend.dtos.PurchaseOrderDto;
import com.bit.backend.entities.GrnEntity;
import com.bit.backend.entities.ProductEntity;
import com.bit.backend.entities.PurchaseOrderDetailEntity;
import com.bit.backend.entities.PurchaseOrderEntity;
import com.bit.backend.mappers.PurchaseOrderMapper;
import com.bit.backend.repositories.GrnRepository;
import com.bit.backend.repositories.PurchaseOrderDetailRepository;
import com.bit.backend.repositories.PurchaseOrderRepository;
import com.bit.backend.repositories.SupplierRepository;

@ExtendWith(MockitoExtension.class)
class PurchaseOrderServiceTest {

    @Mock
    private PurchaseOrderRepository purchaseOrderRepository;

    @Mock
    private PurchaseOrderMapper purchaseOrderMapper;

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private PurchaseOrderDetailRepository purchaseOrderDetailRepository;

    @Mock
    private GrnRepository grnRepository;

    @InjectMocks
    private PurchaseOrderService purchaseOrderService;

    @Test
    void deletePurchaseOrder_shouldDeleteLinkedDetailsAndGrnsBeforeDeletingPurchaseOrder() {
        PurchaseOrderEntity purchaseOrder = new PurchaseOrderEntity();
        purchaseOrder.setId(1L);

        PurchaseOrderDetailEntity detail = new PurchaseOrderDetailEntity();
        detail.setPurchaseOrder(purchaseOrder);
        detail.setProduct(new ProductEntity());

        GrnEntity grn = new GrnEntity();
        grn.setPurchaseOrder(purchaseOrder);
        grn.setProduct(new ProductEntity());

        PurchaseOrderDto dto = new PurchaseOrderDto();
        dto.setId(1L);

        when(purchaseOrderRepository.findById(1L)).thenReturn(Optional.of(purchaseOrder));
        when(purchaseOrderDetailRepository.findByPurchaseOrder_Id(1L)).thenReturn(List.of(detail));
        when(grnRepository.findByPurchaseOrder_Id(1L)).thenReturn(List.of(grn));
        when(purchaseOrderMapper.toDto(purchaseOrder)).thenReturn(dto);

        purchaseOrderService.deletePurchaseOrder(1L);

        verify(purchaseOrderDetailRepository).deleteAll(anyList());
        verify(grnRepository).deleteAll(anyList());
        verify(purchaseOrderRepository).delete(purchaseOrder);
    }
}
