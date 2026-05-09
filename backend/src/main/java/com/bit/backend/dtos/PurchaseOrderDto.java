package com.bit.backend.dtos;
 
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class PurchaseOrderDto {
    private Long id;
    private String orderNumber;
    private Long supplierId;
    private SupplierDto supplier;

    private String orderDate;

    private String status;

    private String expectedDeliveryDate;

    private Double totalAmount;
    private String notes;
}
