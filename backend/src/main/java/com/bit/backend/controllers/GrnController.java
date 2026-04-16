package com.bit.backend.controllers;

import org.springframework.http.HttpStatus;
import com.bit.backend.dtos.GrnDto;
import com.bit.backend.services.GrnService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/grn")
@CrossOrigin(origins = "*")
public class GrnController {

    private final GrnService grnService;

    public GrnController(GrnService grnService) {
        this.grnService = grnService;
    }

    @GetMapping
    public ResponseEntity<List<GrnDto>> getGrnRecords() {
        try {
            List<GrnDto> grnDtoResponse = grnService.getAllGrn();
            return ResponseEntity.ok(grnDtoResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/by-purchase-order/{id}")
    public ResponseEntity<List<GrnDto>> getPurchaseOrderId(@PathVariable Long id) {
        try {
            List<GrnDto> grnDtoist = grnService.getGrnByPurchaseOrderId(id);
            return ResponseEntity.ok(grnDtoist);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<GrnDto> saveGrn(@RequestBody GrnDto dto) {
        try {
            GrnDto grnDtoResponse = grnService.saveGrn(dto);
            return ResponseEntity.ok(grnDtoResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<GrnDto> updateGrn(@PathVariable Long id, @RequestBody GrnDto dto) {
        try {
            GrnDto grnDtoResponse = grnService.updateGrn(id, dto);
            return ResponseEntity.ok(grnDtoResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrn(@PathVariable Long id) {
        try {
            grnService.deleteGrn(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
