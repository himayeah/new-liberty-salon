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

        // Auto-create GRN entry
        GrnEntity grn = new GrnEntity();
        grn.setPurchaseOrder(purchaseOrder);
        grn.setProduct(product);
        grn.setOrderedQuantity(savedEntity.getQuantityOrdered());
        grn.setReceivedQuantity(0.0);
        grn.setOrderedDate(purchaseOrder.getOrderDate());
        grn.setStatus("PENDING");
        grnRepository.save(grn);

        // Update PO Overall Status to PENDING if not already
        if (!"PENDING".equals(purchaseOrder.getStatus()) && !"PARTIAL".equals(purchaseOrder.getStatus()) && !"RECEIVED".equals(purchaseOrder.getStatus())) {
            purchaseOrder.setStatus("PENDING");
        }

        // Update Total Amount
        recalculatePurchaseOrderTotal(purchaseOrder);
        purchaseOrderRepository.save(purchaseOrder);

        return mapper.toDto(savedEntity);
    }

    @Override
    public PurchaseOrderDetailDto updatePurchaseOrderDetail(Long id, PurchaseOrderDetailDto dto) {
        PurchaseOrderDetailEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase Order Detail not found"));
        
        if (dto.getProductId() != null && !dto.getProductId().equals(entity.getProduct().getId())) {
            ProductEntity product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            entity.setProduct(product);
        }
        
        entity.setQuantityOrdered(dto.getQuantityOrdered());
        entity.setUnitPrice(dto.getUnitPrice());
        
        PurchaseOrderDetailEntity savedEntity = repository.save(entity);

        // Update total amount in PO
        PurchaseOrderEntity purchaseOrder = savedEntity.getPurchaseOrder();
        recalculatePurchaseOrderTotal(purchaseOrder);
        purchaseOrderRepository.save(purchaseOrder);

        // Optionally update existing GRN entry if needed
        List<GrnEntity> grns = grnRepository.findByPurchaseOrder_Id(purchaseOrder.getId());
        grns.stream()
            .filter(g -> g.getProduct().getId().equals(savedEntity.getProduct().getId()))
            .findFirst()
            .ifPresent(g -> {
                g.setOrderedQuantity(savedEntity.getQuantityOrdered());
                grnRepository.save(g);
            });

        return mapper.toDto(savedEntity);
    }

    @Override
    public void deletePurchaseOrderDetail(Long id) {
        PurchaseOrderDetailEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase Order Detail not found"));
        
        PurchaseOrderEntity po = entity.getPurchaseOrder();
        
        // Remove linked GRN entry
        List<GrnEntity> grns = grnRepository.findByPurchaseOrder_Id(po.getId());
        grns.stream()
            .filter(g -> g.getProduct().getId().equals(entity.getProduct().getId()))
            .findFirst()
            .ifPresent(grnRepository::delete);
            
        repository.delete(entity);
        
        // Recalculate total
        recalculatePurchaseOrderTotal(po);
        purchaseOrderRepository.save(po);
    }

    private void recalculatePurchaseOrderTotal(PurchaseOrderEntity po) {
        List<PurchaseOrderDetailEntity> details = repository.findByPurchaseOrder_Id(po.getId());
        double total = details.stream()
                .mapToDouble(d -> d.getQuantityOrdered() * d.getUnitPrice())
                .sum();
        po.setTotalAmount(total);
    }
}
