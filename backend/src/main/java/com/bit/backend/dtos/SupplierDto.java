package com.bit.backend.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupplierDto {
    private Long id;
    private String supplierName;
    private String contactName;
    private String phoneNumber;
    private String emailAddress;
    private String address;
    private String paymentTerms;

    public String getSupplierName() {return supplierName;}

}
