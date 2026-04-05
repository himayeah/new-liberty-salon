package com.bit.backend.controllers;

import com.bit.backend.dtos.PurchaseOrderDetailDto;
import com.bit.backend.services.PurchaseOrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-order-details")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PurchaseOrderDetailController {

    private final PurchaseOrderDetailService service;

    @GetMapping
    public ResponseEntity<List<PurchaseOrderDetailDto>> getAll() {
        return ResponseEntity.ok(service.getAllPurchaseOrderDetails());
    }

    @GetMapping("/by-purchase-order/{id}")
    public ResponseEntity<List<PurchaseOrderDetailDto>> getByPurchaseOrderId(@PathVariable Long id) {
        return ResponseEntity.ok(service.getByPurchaseOrderId(id));
    }

    @PostMapping
    public ResponseEntity<PurchaseOrderDetailDto> save(@RequestBody PurchaseOrderDetailDto dto) {
        return ResponseEntity.ok(service.savePurchaseOrderDetail(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseOrderDetailDto> update(@PathVariable Long id, @RequestBody PurchaseOrderDetailDto dto) {
        return ResponseEntity.ok(service.updatePurchaseOrderDetail(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deletePurchaseOrderDetail(id);
        return ResponseEntity.noContent().build();
    }
}
