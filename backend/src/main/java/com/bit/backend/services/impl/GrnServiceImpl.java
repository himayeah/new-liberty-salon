package com.bit.backend.services.impl;

import com.bit.backend.dtos.GrnDto;
import com.bit.backend.entities.GrnEntity;
import com.bit.backend.entities.ProductEntity;
import com.bit.backend.entities.PurchaseOrderEntity;
import com.bit.backend.mappers.GrnMapper;
import com.bit.backend.repositories.ProductRepository;
import com.bit.backend.repositories.PurchaseOrderRepository;
import com.bit.backend.repositories.GrnRepository;
import com.bit.backend.services.GrnService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GrnServiceImpl implements GrnService {

    private final GrnRepository grnRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ProductRepository productRepository;
    private final GrnMapper mapper;

    @Override
    public List<GrnDto> getAllGrn() {
        return mapper.toDtoList(grnRepository.findAll());
    }

    @Override
    public List<GrnDto> getGrnByPurchaseOrderId(Long purchaseOrderId) {
        return mapper.toDtoList(grnRepository.findByPurchaseOrder_Id(purchaseOrderId));
    }

    @Override
    public GrnDto saveGrn(GrnDto dto) {
        GrnEntity entity = mapper.toEntity(dto);

        if (dto.getPurchaseOrderId() != null) {
            PurchaseOrderEntity po = purchaseOrderRepository.findById(dto.getPurchaseOrderId())
                    .orElseThrow(() -> new RuntimeException("Purchase Order not found"));
            entity.setPurchaseOrder(po);
        }

        if (dto.getProductId() != null) {
            ProductEntity product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            entity.setProduct(product);
        }

        // Calculate GRN item status
        entity.setStatus(calculateGrnStatus(entity));

        GrnEntity savedGrn = grnRepository.save(entity);
        updatePurchaseOrderStatus(savedGrn.getPurchaseOrder().getId());
        return mapper.toDto(savedGrn);
    }

    @Override
    public GrnDto updateGrn(Long id, GrnDto dto) {
        GrnEntity entity = grnRepository.findById(id).orElseThrow(() -> new RuntimeException("GRN not found"));

        if (dto.getPurchaseOrderId() != null) {
            PurchaseOrderEntity po = purchaseOrderRepository.findById(dto.getPurchaseOrderId())
                    .orElseThrow(() -> new RuntimeException("Purchase Order not found"));
            entity.setPurchaseOrder(po);
        }

        if (dto.getProductId() != null) {
            ProductEntity product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            entity.setProduct(product);
        }

        entity.setOrderedDate(dto.getOrderedDate());
        entity.setReceivedDate(dto.getReceivedDate());
        entity.setOrderedQuantity(dto.getOrderedQuantity());
        entity.setReceivedQuantity(dto.getReceivedQuantity());
        entity.setRemarks(dto.getRemarks());

        // Calculate GRN item status
        entity.setStatus(calculateGrnStatus(entity));

        GrnEntity savedGrn = grnRepository.save(entity);

        // Sync PO status
        updatePurchaseOrderStatus(savedGrn.getPurchaseOrder().getId());

        return mapper.toDto(savedGrn);
    }

    @Override
    public void deleteGrn(Long id) {
        GrnEntity grn = grnRepository.findById(id).orElse(null);
        if (grn != null) {
            Long poId = grn.getPurchaseOrder().getId();
            grnRepository.delete(grn);
            grnRepository.flush(); // Flush to ensure it's gone before calculating status
            updatePurchaseOrderStatus(poId);
        }
    }

    private String calculateGrnStatus(GrnEntity entity) {
        double ordered = entity.getOrderedQuantity() != null ? entity.getOrderedQuantity() : 0.0;
        double received = entity.getReceivedQuantity() != null ? entity.getReceivedQuantity() : 0.0;

        if (received >= ordered && ordered > 0) {
            return "RECEIVED";
        } else if (received > 0) {
            return "PARTIAL";
        } else if ("CANCELLED".equals(entity.getStatus())) {
            return "CANCELLED";
        }
        return "PENDING";
    }

    private void updatePurchaseOrderStatus(Long poId) {
        PurchaseOrderEntity po = purchaseOrderRepository.findById(poId).orElse(null);
        if (po == null)
            return;

        List<GrnEntity> allGrns = grnRepository.findByPurchaseOrder_Id(poId);
        if (allGrns.isEmpty()) {
            po.setStatus("PENDING");
            purchaseOrderRepository.save(po);
            return;
        }

        boolean allReceived = true;
        boolean anyReceivedOrPartial = false;
        boolean allCancelled = true;

        for (GrnEntity g : allGrns) {
            String s = g.getStatus();
            if (!"RECEIVED".equals(s))
                allReceived = false;
            if ("RECEIVED".equals(s) || "PARTIAL".equals(s))
                anyReceivedOrPartial = true;
            if (!"CANCELLED".equals(s))
                allCancelled = false;
        }

        String newPoStatus = "PENDING";
        if (allCancelled) {
            newPoStatus = "CANCELLED";
        } else if (allReceived) {
            newPoStatus = "RECEIVED";
        } else if (anyReceivedOrPartial) {
            newPoStatus = "PARTIAL";
        }

        po.setStatus(newPoStatus);
        purchaseOrderRepository.save(po);
    }
}
