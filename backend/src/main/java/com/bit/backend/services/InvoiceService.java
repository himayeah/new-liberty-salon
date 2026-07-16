package com.bit.backend.services;

import com.bit.backend.dtos.InvoiceDto;
import java.util.List;

public interface InvoiceService {
    InvoiceDto getInvoiceByBillingId(long billingId);
    List<InvoiceDto> getInvoices();
}
