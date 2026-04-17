package com.bit.backend.services.impl;

import com.bit.backend.dtos.GrnDto;
import com.bit.backend.entities.GrnEntity;
import com.bit.backend.entities.ProductEntity;
import com.bit.backend.entities.PurchaseOrderEntity;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.mappers.GrnMapper;
import com.bit.backend.repositories.ProductRepository;
import com.bit.backend.repositories.PurchaseOrderRepository;
import com.bit.backend.repositories.GrnRepository;
import com.bit.backend.services.GrnService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

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

        try {
            List<GrnEntity> grnEntityList = grnRepository.findAll();
            List<GrnDto> grnDtoList = mapper.toDtoList(grnEntityList);
            return grnDtoList;
        } catch (Exception e) {
            throw new AppException("Request failed with error" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public GrnDto getGrnByPurchaseOrderId(long id) {
        try {
            Optional<GrnEntity> optionalGrnEntity = grnRepository.findById(id);
            if (!optionalGrnEntity.isPresent()) {
                throw new AppException("Grn Does Not Exist", HttpStatus.NOT_FOUND);
            }
            return mapper.toDto(optionalGrnEntity.get());
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

    @Override
    public List<GrnDto> getGrnByPurchaseOrderId(Long purchaseOrderId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getGrnByPurchaseOrderId'");
    }
}
