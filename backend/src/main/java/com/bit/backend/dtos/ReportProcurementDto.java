package com.bit.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ReportProcurementDto {

    private String orderNumber;
    private String supplier;
    private String orderDate;
    private String expectedDate;
    private String lateDays;
    private String totalWorth;

    private String totalAmount;

}
