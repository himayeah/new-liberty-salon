package com.bit.backend.services.impl;

import com.bit.backend.dtos.PurchaseOrderDetailDto;
import com.bit.backend.entities.PurchaseOrderDetailEntity;
import com.bit.backend.entities.ProductEntity;
import com.bit.backend.mappers.PurchaseOrderDetailMapper;
import com.bit.backend.repositories.ProductRepository;
import com.bit.backend.repositories.PurchaseOrderDetailRepository;
import com.bit.backend.services.PurchaseOrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.bit.backend.entities.GrnEntity;
import com.bit.backend.entities.PurchaseOrderEntity;
import com.bit.backend.repositories.GrnRepository;
import com.bit.backend.repositories.PurchaseOrderRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseOrderDetailServiceImpl implements PurchaseOrderDetailService {

    private final PurchaseOrderDetailRepository repository;
    private final ProductRepository productRepository;
    private final GrnRepository grnRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderDetailMapper mapper;

    @Override
    public List<PurchaseOrderDetailDto> getAllPurchaseOrderDetails() {
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public List<PurchaseOrderDetailDto> getByPurchaseOrderId(Long purchaseOrderId) {
        return mapper.toDtoList(repository.findByPurchaseOrder_Id(purchaseOrderId));
    }

    @Override
    public PurchaseOrderDetailDto savePurchaseOrderDetail(PurchaseOrderDetailDto dto) {
        // Fetch full entities first
        PurchaseOrderEntity purchaseOrder = purchaseOrderRepository.findById(dto.getPurchaseOrderId())
                .orElseThrow(() -> new RuntimeException("Purchase Order not found"));
        ProductEntity product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        PurchaseOrderDetailEntity entity = mapper.toEntity(dto);
        // Ensure entities are fully populated 
        entity.setPurchaseOrder(purchaseOrder);
        entity.setProduct(product);
        
        PurchaseOrderDetailEntity savedEntity = repository.save(entity);
        repository.flush(); // Ensure it's in DB for recalculation

        // Auto-create GRN entry
        GrnEntity grn = new GrnEntity();
        grn.setPurchaseOrder(purchaseOrder);
        grn.setProduct(product);
        grn.setOrderedQuantity(savedEntity.getQuantityOrdered());
        grn.setReceivedQuantity(0.0);
        grn.setOrderedDate(purchaseOrder.getOrderDate());
        grn.setStatus("PENDING");
        grnRepository.save(grn);

        // Update PO Overall Status using shared logic if possible, or replicate
        updatePurchaseOrderStatus(purchaseOrder.getId());

        // Update Total Amount
        recalculatePurchaseOrderTotal(purchaseOrder);
        purchaseOrderRepository.save(purchaseOrder);

        return mapper.toDto(savedEntity);
    }

    @Override
    public PurchaseOrderDetailDto updatePurchaseOrderDetail(Long id, PurchaseOrderDetailDto dto) {
        PurchaseOrderDetailEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase Order Detail not found"));
        
        Long oldProductId = entity.getProduct().getId();
        Double oldQuantity = entity.getQuantityOrdered();

        if (dto.getProductId() != null && !dto.getProductId().equals(oldProductId)) {
            ProductEntity product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            entity.setProduct(product);
        }
        
        entity.setQuantityOrdered(dto.getQuantityOrdered());
        entity.setUnitPrice(dto.getUnitPrice());
        
        PurchaseOrderDetailEntity savedEntity = repository.save(entity);
        repository.flush();

        // Update total amount in PO
        PurchaseOrderEntity purchaseOrder = savedEntity.getPurchaseOrder();
        recalculatePurchaseOrderTotal(purchaseOrder);
        purchaseOrderRepository.save(purchaseOrder);

        // Update existing GRN entry
        List<GrnEntity> grns = grnRepository.findByPurchaseOrder_Id(purchaseOrder.getId());
        
        // If product changed, we need to find the GRN that had the old product
        if (!entity.getProduct().getId().equals(oldProductId)) {
            grns.stream()
                .filter(g -> g.getProduct().getId().equals(oldProductId))
                .findFirst()
                .ifPresent(g -> {
                    g.setProduct(savedEntity.getProduct());
                    g.setOrderedQuantity(savedEntity.getQuantityOrdered());
                    grnRepository.save(g);
                });
        } else {
            // Product didn't change, just update quantity
            grns.stream()
                .filter(g -> g.getProduct().getId().equals(savedEntity.getProduct().getId()))
                .findFirst()
                .ifPresent(g -> {
                    g.setOrderedQuantity(savedEntity.getQuantityOrdered());
                    grnRepository.save(g);
                });
        }

        updatePurchaseOrderStatus(purchaseOrder.getId());

        return mapper.toDto(savedEntity);
    }

    @Override
    public void deletePurchaseOrderDetail(Long id) {
        PurchaseOrderDetailEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase Order Detail not found"));
        
        PurchaseOrderEntity po = entity.getPurchaseOrder();
        Long productId = entity.getProduct().getId();
        
        repository.delete(entity);
        repository.flush(); // Ensure it's gone for recalculation
        
        // Remove linked GRN entry
        List<GrnEntity> grns = grnRepository.findByPurchaseOrder_Id(po.getId());
        grns.stream()
            .filter(g -> g.getProduct().getId().equals(productId))
            .findFirst()
            .ifPresent(grnRepository::delete);
            
        // Recalculate total
        recalculatePurchaseOrderTotal(po);
        updatePurchaseOrderStatus(po.getId());
        purchaseOrderRepository.save(po);
    }

    private void recalculatePurchaseOrderTotal(PurchaseOrderEntity po) {
        List<PurchaseOrderDetailEntity> details = repository.findByPurchaseOrder_Id(po.getId());
        double total = details.stream()
                .mapToDouble(d -> {
                    double qty = d.getQuantityOrdered() != null ? d.getQuantityOrdered() : 0.0;
                    double price = d.getUnitPrice() != null ? d.getUnitPrice() : 0.0;
                    return qty * price;
                })
                .sum();
        po.setTotalAmount(total);
    }

    private void updatePurchaseOrderStatus(Long poId) {
        PurchaseOrderEntity po = purchaseOrderRepository.findById(poId).orElse(null);
        if (po == null) return;

        List<GrnEntity> allGrns = grnRepository.findByPurchaseOrder_Id(poId);
        if (allGrns.isEmpty()) {
            po.setStatus("PENDING"); // Or whatever default
            return;
        }

        boolean allReceived = true;
        boolean anyReceivedOrPartial = false;
        boolean allCancelled = true;

        for (GrnEntity g : allGrns) {
            String s = g.getStatus();
            if (!"RECEIVED".equals(s)) allReceived = false;
            if ("RECEIVED".equals(s) || "PARTIAL".equals(s)) anyReceivedOrPartial = true;
            if (!"CANCELLED".equals(s)) allCancelled = false;
        }

        String newStatus = "PENDING";
        if (allCancelled) newStatus = "CANCELLED";
        else if (allReceived) newStatus = "RECEIVED";
        else if (anyReceivedOrPartial) newStatus = "PARTIAL";

        po.setStatus(newStatus);
    }
}

