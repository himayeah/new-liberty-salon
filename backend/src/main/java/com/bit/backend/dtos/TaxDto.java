package com.bit.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaxDto {

    private String taxName;
    private String taxRate;
    private String effectiveDate;
    private String isActive;

    public String getTaxName() {return taxName;}


}
