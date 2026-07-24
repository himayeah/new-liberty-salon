package com.bit.backend.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bit.backend.dtos.PurchaseOrderDto;
import com.bit.backend.entities.GrnEntity;
import com.bit.backend.entities.PurchaseOrderDetailEntity;
import com.bit.backend.entities.PurchaseOrderEntity;
import com.bit.backend.entities.SupplierEntity;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.mappers.PurchaseOrderMapper;
import com.bit.backend.repositories.GrnRepository;
import com.bit.backend.repositories.PurchaseOrderDetailRepository;
import com.bit.backend.repositories.PurchaseOrderRepository;
import com.bit.backend.repositories.SupplierRepository;
import com.bit.backend.services.PurchaseOrderServiceI;

@Service
@Transactional
public class PurchaseOrderService implements PurchaseOrderServiceI {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderMapper purchaseOrderMapper;
    private final SupplierRepository supplierRepository;
    private final PurchaseOrderDetailRepository purchaseOrderDetailRepository;
    private final GrnRepository grnRepository;

    public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository,
            PurchaseOrderMapper purchaseOrderMapper,
            SupplierRepository supplierRepository,
            PurchaseOrderDetailRepository purchaseOrderDetailRepository,
            GrnRepository grnRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.purchaseOrderMapper = purchaseOrderMapper;
        this.supplierRepository = supplierRepository;
        this.purchaseOrderDetailRepository = purchaseOrderDetailRepository;
        this.grnRepository = grnRepository;
    }

    private String generateNextOrderNumber() {
        String lastOrderNum = purchaseOrderRepository.getLastOrderNumber();
        if (lastOrderNum == null || !lastOrderNum.startsWith("PO-")) {
            return "PO-1001";
        }
        try {
            String numStr = lastOrderNum.substring(3);
            int nextNum = Integer.parseInt(numStr) + 1;
            return "PO-" + nextNum;
        } catch (NumberFormatException e) {
            return "PO-" + System.currentTimeMillis();
        }
    }

    @Override
    public PurchaseOrderDto addPurchaseOrder(PurchaseOrderDto purchaseOrderDto) {
        if (purchaseOrderDto.getSupplierId() == null) {
            throw new AppException("Supplier ID is required", HttpStatus.BAD_REQUEST);
        }

        PurchaseOrderEntity entity = purchaseOrderMapper.toEntity(purchaseOrderDto);
        entity.setOrderNumber(generateNextOrderNumber());

        SupplierEntity supplier = supplierRepository.findById(purchaseOrderDto.getSupplierId())
                .orElseThrow(() -> new AppException("Supplier not found", HttpStatus.NOT_FOUND));

        entity.setSupplier(supplier);
        PurchaseOrderEntity saved = purchaseOrderRepository.save(entity);
        return purchaseOrderMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrderDto> getPurchaseOrders() {
        return purchaseOrderRepository.findAll().stream()
                .map(purchaseOrderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PurchaseOrderDto updatePurchaseOrder(Long id, PurchaseOrderDto dto) {
        PurchaseOrderEntity existing = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new AppException("Purchase Order not found", HttpStatus.NOT_FOUND));

        if (dto.getOrderNumber() != null && !dto.getOrderNumber().isBlank()) {
            existing.setOrderNumber(dto.getOrderNumber());
        }
        existing.setOrderDate(dto.getOrderDate());
        existing.setStatus(dto.getStatus());
        existing.setExpectedDeliveryDate(dto.getExpectedDeliveryDate());
        existing.setTotalAmount(dto.getTotalAmount());
        existing.setNotes(dto.getNotes());

        if (dto.getSupplierId() != null) {
            SupplierEntity supplier = supplierRepository.findById(dto.getSupplierId())
                    .orElseThrow(() -> new AppException("Supplier not found", HttpStatus.NOT_FOUND));
            existing.setSupplier(supplier);
        }

        PurchaseOrderEntity updated = purchaseOrderRepository.save(existing);
        return purchaseOrderMapper.toDto(updated);
    }

    @Override
    public PurchaseOrderDto deletePurchaseOrder(Long id) {
        PurchaseOrderEntity existing = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new AppException("Purchase Order not found", HttpStatus.NOT_FOUND));

        List<PurchaseOrderDetailEntity> details = purchaseOrderDetailRepository.findByPurchaseOrder_Id(id);
        if (!details.isEmpty()) {
            purchaseOrderDetailRepository.deleteAll(details);
        }

        List<GrnEntity> grns = grnRepository.findByPurchaseOrder_Id(id);
        if (!grns.isEmpty()) {
            grnRepository.deleteAll(grns);
        }

        purchaseOrderRepository.delete(existing);
        return purchaseOrderMapper.toDto(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseOrderDto getPurchaseOrderById(Long id) {
        PurchaseOrderEntity existing = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new AppException("Purchase Order not found", HttpStatus.NOT_FOUND));
        return purchaseOrderMapper.toDto(existing);
    }
}
