package com.bit.backend.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseOrderDetailDto {
    private Long id;
    private Long purchaseOrderId;
    private Long productId;
    private String productName;
    private Double quantityOrdered;
    private Double unitPrice;
}
