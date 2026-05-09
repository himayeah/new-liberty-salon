package com.bit.backend.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GrnDto {
    private Long id;
    private Long purchaseOrderId;
    private Long productId;
    private String productName;
    private String orderedDate;
    private String receivedDate;
    private Double orderedQuantity;
    private Double receivedQuantity;
    private String remarks;
    private String status;
}
