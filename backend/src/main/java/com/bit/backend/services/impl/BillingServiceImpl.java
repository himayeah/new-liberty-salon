package com.bit.backend.services.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bit.backend.dtos.BillingDto;
import com.bit.backend.entities.BillingEntity;
import com.bit.backend.entities.BillingPurchaseEntity;
import com.bit.backend.entities.InventoryEntity;
import com.bit.backend.entities.InvoiceEntity;
import com.bit.backend.entities.InvoiceItemEntity;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.mappers.BillingMapper;
import com.bit.backend.repositories.BillingRepository;
import com.bit.backend.repositories.InventoryRepository;
import com.bit.backend.repositories.InvoiceRepository;
import com.bit.backend.repositories.ProductRepository;
import com.bit.backend.repositories.ServiceRepository;
import com.bit.backend.repositories.TaxRepository;
import com.bit.backend.services.BillingService;

@Service
@Transactional
public class BillingServiceImpl implements BillingService {

    private final BillingRepository billingRepository;
    private final BillingMapper billingMapper;
    private final InventoryRepository inventoryRepository;
    private final TaxRepository taxRepository;
    private final ProductRepository productRepository;
    private final ServiceRepository serviceRepository;
    private final InvoiceRepository invoiceRepository;

    public BillingServiceImpl(BillingRepository billingRepository, BillingMapper billingMapper,
                              InventoryRepository inventoryRepository, TaxRepository taxRepository,
                              ProductRepository productRepository, ServiceRepository serviceRepository,
                              InvoiceRepository invoiceRepository) {
        this.billingMapper = billingMapper;
        this.billingRepository = billingRepository;
        this.inventoryRepository = inventoryRepository;
        this.taxRepository = taxRepository;
        this.productRepository = productRepository;
        this.serviceRepository = serviceRepository;
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public BillingDto addBilling(BillingDto dto) {
        try {
            BillingEntity entity = billingMapper.toBillingEntity(dto);
            if (entity.getPurchases() != null) {
                for (BillingPurchaseEntity p : entity.getPurchases()) {
                    p.setBilling(entity);
                    if ("PRODUCT PURCHASE".equals(p.getCategory()) && p.getName() != null) {
                        java.util.Optional<InventoryEntity> invOpt = inventoryRepository.findByProductProductName(p.getName());
                        if (invOpt.isPresent()) {
                            InventoryEntity inv = invOpt.get();
                            int qty = p.getQuantity() != null ? p.getQuantity() : 0;
                            inv.setCurrentStock(inv.getCurrentStock() - qty);
                            inventoryRepository.save(inv);
                        }
                    }
                }
            }
            BillingEntity savedItem = billingRepository.save(entity);

            // Generate Invoice and Invoice Items
            generateInvoice(savedItem);

            return billingMapper.toBillingDto(savedItem);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.BAD_REQUEST);
        }
    }

    private void generateInvoice(BillingEntity billing) {
        double taxRate = 0.0;
        try {
            java.util.List<com.bit.backend.entities.TaxEntity> taxes = taxRepository.findAll();
            for (com.bit.backend.entities.TaxEntity t : taxes) {
                if (t.getIsActive() != null && ("true".equalsIgnoreCase(t.getIsActive()) || "1".equals(t.getIsActive()) || "active".equalsIgnoreCase(t.getIsActive()))) {
                    try {
                        taxRate = Double.parseDouble(t.getTaxRate());
                    } catch (Exception ignored) {}
                    break;
                }
            }
        } catch (Exception ignored) {}

        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setBilling(billing);
        invoice.setClientName(billing.getClientName());
        invoice.setInvoiceDate(billing.getBillingDate());
        invoice.setDueDate(billing.getBillingDate());
        invoice.setPaymentStatus(billing.getPaymentStatus() != null ? billing.getPaymentStatus() : "Completed");

        double totalAmount = 0.0;
        java.util.List<InvoiceItemEntity> invoiceItems = new java.util.ArrayList<>();

        if (billing.getPurchases() != null) {
            for (BillingPurchaseEntity p : billing.getPurchases()) {
                double qty = p.getQuantity() != null ? p.getQuantity().doubleValue() : 1.0;
                double price = p.getPrice() != null ? p.getPrice() : 0.0;
                double lineTotal = qty * price;
                totalAmount += lineTotal;

                InvoiceItemEntity item = new InvoiceItemEntity();
                item.setInvoice(invoice);
                item.setLineItemType(p.getCategory());
                item.setServiceProductName(p.getName());
                
                if ("PRODUCT PURCHASE".equals(p.getCategory())) {
                    java.util.Optional<com.bit.backend.entities.ProductEntity> prodOpt = productRepository.findByProductName(p.getName());
                    prodOpt.ifPresent(productEntity -> item.setServiceProductId(productEntity.getId()));
                } else if ("SERVICE".equals(p.getCategory())) {
                    java.util.Optional<com.bit.backend.entities.ServiceEntity> servOpt = serviceRepository.findByServiceName(p.getName());
                    servOpt.ifPresent(serviceEntity -> item.setServiceProductId(serviceEntity.getId()));
                }

                item.setQuantity(qty);
                item.setUnitPrice(price);
                item.setLineTotal(lineTotal);
                item.setStaffCommission(0.0);

                invoiceItems.add(item);
            }
        }

        invoice.setTotalAmount(totalAmount);
        invoice.setDiscountAmount(0.0);
        double taxAmount = totalAmount * (taxRate / 100.0);
        invoice.setTaxAmount(taxAmount);
        double netTotal = totalAmount + taxAmount;
        invoice.setTotalAmountAfterTaxAndDiscount(netTotal);
        invoice.setAmountPaid(netTotal);
        invoice.setBalanceDue(0.0);

        invoice.setItems(invoiceItems);

        InvoiceEntity savedInvoice = invoiceRepository.save(invoice);
        savedInvoice.setInvoiceNumber("INV-" + String.format("%06d", savedInvoice.getId()));
        invoiceRepository.save(savedInvoice);
    }

    @Override
    public java.util.List<BillingDto> getBilling() {
        try {
            java.util.List<BillingEntity> entities = billingRepository.findAll();
            return billingMapper.toBillingDtoList(entities);
        } catch (Exception e) {
            throw new AppException("Request failed with error: " + e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public BillingDto updateBilling(long id, BillingDto dto) {
        try {
            if (!billingRepository.existsById(id)) {
                throw new AppException("Billing not found with id: " + id, HttpStatus.NOT_FOUND);
            }
            BillingEntity entity = billingMapper.toBillingEntity(dto);
            entity.setId(id);
            if (entity.getPurchases() != null) {
                entity.getPurchases().forEach(p -> p.setBilling(entity));
            }
            BillingEntity updatedItem = billingRepository.save(entity);
            return billingMapper.toBillingDto(updatedItem);
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException("Update failed with error:" + e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public BillingDto deleteBilling(long id) {
        try {
            java.util.Optional<BillingEntity> entityOpt = billingRepository.findById(id);
            if (entityOpt.isPresent()) {
                billingRepository.delete(entityOpt.get());
                return billingMapper.toBillingDto(entityOpt.get());
            } else {
                throw new AppException("Billing not found with id: " + id, HttpStatus.NOT_FOUND);
            }
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException("Request failed with error: " + e, HttpStatus.BAD_REQUEST);
        }
    }
}
