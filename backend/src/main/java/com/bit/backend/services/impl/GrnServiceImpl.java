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

    private final GrnRepository repository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ProductRepository productRepository;
    private final GrnMapper mapper;

    @Override
    public List<GrnDto> getAllGrn() {
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public List<GrnDto> getGrnByPurchaseOrderId(Long purchaseOrderId) {
        return mapper.toDtoList(repository.findByPurchaseOrder_Id(purchaseOrderId));
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

        String status = "PENDING";
        if (entity.getReceivedQuantity() != null && entity.getOrderedQuantity() != null) {
            if (entity.getReceivedQuantity() >= entity.getOrderedQuantity()) {
                status = "RECEIVED";
            } else if (entity.getReceivedQuantity() > 0) {
                status = "PARTIAL";
            } else if ("CANCELLED".equals(dto.getStatus())) {
                status = "CANCELLED";
            }
        }
        entity.setStatus(status);

        GrnEntity savedGrn = repository.save(entity);
        updatePurchaseOrderStatus(savedGrn.getPurchaseOrder().getId());
        return mapper.toDto(savedGrn);
    }

    @Override
    public GrnDto updateGrn(Long id, GrnDto dto) {
        GrnEntity entity = repository.findById(id).orElseThrow(() -> new RuntimeException("GRN not found"));

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
        String status = "PENDING";
        if (entity.getReceivedQuantity() != null && entity.getOrderedQuantity() != null) {
            if (entity.getReceivedQuantity() >= entity.getOrderedQuantity()) {
                status = "RECEIVED";
            } else if (entity.getReceivedQuantity() > 0) {
                status = "PARTIAL";
            } else if ("CANCELLED".equals(dto.getStatus())) {
                status = "CANCELLED";
            }
        }
        entity.setStatus(status);

        GrnEntity savedGrn = repository.save(entity);

        // Sync PO status
        updatePurchaseOrderStatus(savedGrn.getPurchaseOrder().getId());

        return mapper.toDto(savedGrn);
    }

    private void updatePurchaseOrderStatus(Long poId) {
        PurchaseOrderEntity po = purchaseOrderRepository.findById(poId).orElse(null);
        if (po == null)
            return;

        List<GrnEntity> allGrns = repository.findByPurchaseOrder_Id(poId);
        if (allGrns.isEmpty())
            return;

        boolean allReceived = true;
        boolean anyReceived = false;
        boolean anyPartial = false;
        boolean allCancelled = true;

        for (GrnEntity g : allGrns) {
            if (!"RECEIVED".equals(g.getStatus()))
                allReceived = false;
            if ("RECEIVED".equals(g.getStatus()) || "PARTIAL".equals(g.getStatus()))
                anyReceived = true;
            if ("PARTIAL".equals(g.getStatus()))
                anyPartial = true;
            if (!"CANCELLED".equals(g.getStatus()))
                allCancelled = false;
        }

        String newPoStatus = "PENDING";
        if (allCancelled) {
            newPoStatus = "CANCELLED";
        } else if (allReceived) {
            newPoStatus = "RECEIVED";
        } else if (anyReceived || anyPartial) {
            newPoStatus = "PARTIAL";
        }

        po.setStatus(newPoStatus);
        purchaseOrderRepository.save(po);
    }

    @Override
    public void deleteGrn(Long id) {
        repository.deleteById(id);
    }
}
