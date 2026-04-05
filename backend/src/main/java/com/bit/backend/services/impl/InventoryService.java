package com.bit.backend.services.impl;

import com.bit.backend.dtos.InventoryDto;
import com.bit.backend.entities.InventoryEntity;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.mappers.InventoryMapper;
import com.bit.backend.repositories.InventoryRepository;
import com.bit.backend.services.InventoryServiceI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService implements InventoryServiceI {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;

    @Override
    public InventoryDto addInventory(InventoryDto inventoryDto) {
        try {
            InventoryEntity entity = inventoryMapper.toEntity(inventoryDto);
            return inventoryMapper.toDto(inventoryRepository.save(entity));
        } catch (Exception e) {
            throw new AppException("Failed to add inventory: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<InventoryDto> getData() {
        return inventoryMapper.toDtoList(inventoryRepository.findAll());
    }

    @Override
    public InventoryDto updateInventory(Long id, InventoryDto inventoryDto) {
        if (!inventoryRepository.existsById(id)) {
            throw new AppException("Inventory record not found", HttpStatus.NOT_FOUND);
        }
        inventoryDto.setId(id);
        InventoryEntity entity = inventoryMapper.toEntity(inventoryDto);
        return inventoryMapper.toDto(inventoryRepository.save(entity));
    }

    @Override
    public InventoryDto deleteInventory(Long id) {
        InventoryEntity entity = inventoryRepository.findById(id)
                .orElseThrow(() -> new AppException("Inventory record not found", HttpStatus.NOT_FOUND));
        inventoryRepository.deleteById(id);
        return inventoryMapper.toDto(entity);
    }
}
