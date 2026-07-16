package com.bit.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemDto {
    private Long id;
    private String lineItemType;
    private String serviceProductName;
    private Long serviceProductId;
    private Double quantity;
    private Double unitPrice;
    private Double lineTotal;
    private Double staffCommission;
}
