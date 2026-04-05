package com.bit.backend.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class PurchaseOrderDto {
    private Long id;
    private String orderNumber;
    private Long supplierId;
    private SupplierDto supplier;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate orderDate;

    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expectedDeliveryDate;

    private Double totalAmount;
    private String notes;
}
