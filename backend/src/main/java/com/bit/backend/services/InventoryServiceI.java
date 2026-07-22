package com.bit.backend.services;

import com.bit.backend.dtos.InventoryDto;
import java.util.List;

public interface InventoryServiceI {
    InventoryDto addInventory(InventoryDto inventoryDto);
    List<InventoryDto> getData();
    InventoryDto updateInventory(Long id, InventoryDto inventoryDto);
    InventoryDto deleteInventory(Long id);

    // Fetch all inventory items that have reached or dropped below re-order limits
    List<InventoryDto> getReorderAlerts();
}
