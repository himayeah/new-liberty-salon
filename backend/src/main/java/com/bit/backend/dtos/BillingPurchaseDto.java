package com.bit.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillingPurchaseDto {
    private Long id;
    private String category;
    private String name;
    private Integer quantity;
    private Double price;
}
