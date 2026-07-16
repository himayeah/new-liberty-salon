package com.bit.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDto {
    private Long id;
    private String invoiceNumber;
    private String clientName;
    private String invoiceDate;
    private String dueDate;
    private String paymentStatus;
    private Double totalAmount;
    private Double discountAmount;
    private Double taxAmount;
    private Double totalAmountAfterTaxAndDiscount;
    private Double amountPaid;
    private Double balanceDue;
    private Long billingId;
    private List<InvoiceItemDto> items;
}
