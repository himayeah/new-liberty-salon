package com.bit.backend.controllers;

import com.bit.backend.dtos.PurchaseOrderDto;
import com.bit.backend.services.PurchaseOrderServiceI;
import com.bit.backend.exceptions.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/purchase-orders")
public class PurchaseOrderController {

    private final PurchaseOrderServiceI purchaseOrderService;

    public PurchaseOrderController(PurchaseOrderServiceI purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    @PostMapping
    public ResponseEntity<PurchaseOrderDto> addPurchaseOrder(@RequestBody PurchaseOrderDto purchaseOrderDto) {
        try {
            PurchaseOrderDto response = purchaseOrderService.addPurchaseOrder(purchaseOrderDto);
            return ResponseEntity.created(URI.create("/api/purchase-orders/" + response.getId()))
                    .body(response);
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException("Failed to add purchase order: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<PurchaseOrderDto>> getPurchaseOrders() {
        try {
            List<PurchaseOrderDto> list = purchaseOrderService.getPurchaseOrders();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            throw new AppException("Failed to fetch purchase orders: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrderDto> getPurchaseOrderById(@PathVariable Long id) {
        try {
            PurchaseOrderDto response = purchaseOrderService.getPurchaseOrderById(id);
            return ResponseEntity.ok(response);
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException("Failed to fetch purchase order: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseOrderDto> updatePurchaseOrder(@PathVariable Long id, @RequestBody PurchaseOrderDto purchaseOrderDto) {
        try {
            PurchaseOrderDto response = purchaseOrderService.updatePurchaseOrder(id, purchaseOrderDto);
            return ResponseEntity.ok(response);
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException("Failed to update purchase order: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PurchaseOrderDto> deletePurchaseOrder(@PathVariable Long id) {
        try {
            PurchaseOrderDto response = purchaseOrderService.deletePurchaseOrder(id);
            return ResponseEntity.ok(response);
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException("Failed to delete purchase order: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
