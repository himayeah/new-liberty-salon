package com.bit.backend.controllers;

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

    private final GrnService service;

    public GrnController(GrnService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<GrnDto>> getAll() {
        return ResponseEntity.ok(service.getAllGrn());
    }

    @GetMapping("/by-purchase-order/{id}")
    public ResponseEntity<List<GrnDto>> getByPurchaseOrderId(@PathVariable Long id) {
        return ResponseEntity.ok(service.getGrnByPurchaseOrderId(id));
    }

    @PostMapping
    public ResponseEntity<GrnDto> save(@RequestBody GrnDto dto) {
        return ResponseEntity.ok(service.saveGrn(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GrnDto> update(@PathVariable Long id, @RequestBody GrnDto dto) {
        return ResponseEntity.ok(service.updateGrn(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteGrn(id);
        return ResponseEntity.noContent().build();
    }
}
