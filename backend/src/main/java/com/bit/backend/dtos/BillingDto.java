package com.bit.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillingDto {

    private Long id;
    private String clientName;
    private String billingCategory;
    private Long clientType;
    private String paymentStatus;
    private Long billingDate;
    private String paymentMethod;

}
