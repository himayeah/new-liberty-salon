package com.bit.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryDto {
    private Long id;
    private Long productId;
    private ProductDto product;
    private int currentStock;
    private int minimumStock;
    private int maximumStock;
    private LocalDate lastRestockedDate;
    private String shelfLocation;
}
