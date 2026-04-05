package com.bit.backend.controllers;

import com.bit.backend.dtos.InventoryDto;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.services.InventoryServiceI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryServiceI inventoryService;

    @PostMapping
    public ResponseEntity<InventoryDto> addInventory(@RequestBody InventoryDto inventoryDto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(inventoryService.addInventory(inventoryDto));
        } catch (Exception e) {
            throw new AppException("Request failed with error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<InventoryDto>> getData() {
        try {
            return ResponseEntity.ok(inventoryService.getData());
        } catch (Exception e) {
            throw new AppException("Request failed with error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryDto> updateInventory(@PathVariable Long id, @RequestBody InventoryDto inventoryDto) {
        try {
            return ResponseEntity.ok(inventoryService.updateInventory(id, inventoryDto));
        } catch (Exception e) {
            throw new AppException("Request failed with error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<InventoryDto> deleteInventory(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(inventoryService.deleteInventory(id));
        } catch (Exception e) {
            throw new AppException("Request failed with error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
