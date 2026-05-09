package com.bit.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


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
    private String lastRestockedDate;
    private String shelfLocation;
}
