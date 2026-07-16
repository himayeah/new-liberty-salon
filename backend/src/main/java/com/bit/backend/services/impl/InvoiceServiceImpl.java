package com.bit.backend.services.impl;

import com.bit.backend.dtos.InvoiceDto;
import com.bit.backend.entities.InvoiceEntity;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.mappers.InvoiceMapper;
import com.bit.backend.repositories.InvoiceRepository;
import com.bit.backend.services.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, InvoiceMapper invoiceMapper) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceMapper = invoiceMapper;
    }

    @Override
    public InvoiceDto getInvoiceByBillingId(long billingId) {
        try {
            Optional<InvoiceEntity> invoiceOpt = invoiceRepository.findByBillingId(billingId);
            if (invoiceOpt.isPresent()) {
                return invoiceMapper.toInvoiceDto(invoiceOpt.get());
            } else {
                throw new AppException("Invoice not found for billing id: " + billingId, HttpStatus.NOT_FOUND);
            }
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException("Request failed with error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<InvoiceDto> getInvoices() {
        try {
            List<InvoiceEntity> entities = invoiceRepository.findAll();
            return invoiceMapper.toInvoiceDtoList(entities);
        } catch (Exception e) {
            throw new AppException("Request failed with error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
