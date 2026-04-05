package com.bit.backend.dtos;

import lombok.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GrnDto {
    private Long id;
    private Long purchaseOrderId;
    private Long productId;
    private String productName;
    private LocalDate orderedDate;
    private LocalDate receivedDate;
    private Double orderedQuantity;
    private Double receivedQuantity;
    private String remarks;
    private String status;
}
