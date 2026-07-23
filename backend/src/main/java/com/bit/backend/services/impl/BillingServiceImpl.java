package com.bit.backend.services.impl;

import java.util.Optional;
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

    // Add billing- HND
    public BillingDto addBilling(BillingDto billingdto) {
        try {
            BillingEntity billingEntity = billingMapper.toBillingEntity(billingdto);

            if (billingEntity.getPurchases() != null) {
                // loops through each purchase item in the billing
                for (BillingPurchaseEntity billingPurchaseEntity : billingEntity.getPurchases()) {
                    billingPurchaseEntity.setBilling(billingEntity);
                    resolveBillingPurchaseIds(billingPurchaseEntity);

                    // deduct product purchases from Inventory
                    if ("PRODUCT PURCHASE".equalsIgnoreCase(billingPurchaseEntity.getCategory())) {

                        // Creates an empty variable first. Later it will store the found inventory
                        // record
                        Optional<InventoryEntity> inventoryOptional = Optional.empty();

                        if (billingPurchaseEntity.getProductId() != null) {
                            // returns the entire product record for the selected productId
                            inventoryOptional = inventoryRepository
                                    .findByProductId(billingPurchaseEntity.getProductId());
                        } else if (billingPurchaseEntity.getName() != null) {
                            // checks by the product name also
                            inventoryOptional = inventoryRepository
                                    .findByProductProductName(billingPurchaseEntity.getName());
                        }

                        if (inventoryOptional.isPresent()) {

                            // optional is a wrapper. so here we get the actual entity record
                            InventoryEntity inventory = inventoryOptional.get();

                            // do the stock update
                            inventory
                                    .setCurrentStock(inventory.getCurrentStock() - billingPurchaseEntity.getQuantity());

                            inventoryRepository.save(inventory);
                        }
                    }
                }
            }
            BillingEntity savedBilling = billingRepository.save(billingEntity);

            // Generate Invoice and Invoice Items
            generateInvoice(savedBilling);

            return billingMapper.toBillingDto(savedBilling);
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.BAD_REQUEST);
        }
    }

    private void resolveBillingPurchaseIds(BillingPurchaseEntity billingPurchaseEntity) {
        if (billingPurchaseEntity == null) {
            return;
        }

        if (billingPurchaseEntity.getCategory() != null) {
            String category = billingPurchaseEntity.getCategory().trim();

            if (billingPurchaseEntity.getProductId() == null && "PRODUCT PURCHASE".equalsIgnoreCase(category)
                    && billingPurchaseEntity.getName() != null) {
                java.util.Optional<com.bit.backend.entities.ProductEntity> prodOpt =
                        productRepository.findByProductName(billingPurchaseEntity.getName());
                prodOpt.ifPresent(productEntity -> billingPurchaseEntity.setProductId(productEntity.getId()));
            }

            if (billingPurchaseEntity.getServiceId() == null && "SERVICE".equalsIgnoreCase(category)
                    && billingPurchaseEntity.getName() != null) {
                java.util.Optional<com.bit.backend.entities.ServiceEntity> servOpt =
                        serviceRepository.findByServiceName(billingPurchaseEntity.getName());
                servOpt.ifPresent(serviceEntity -> billingPurchaseEntity.setServiceId(serviceEntity.getId()));
            }
        }
    }

    private void generateInvoice(BillingEntity billing) {
        double taxRate = 0.0;
        try {
            java.util.List<com.bit.backend.entities.TaxEntity> taxes = taxRepository.findAll();
            for (com.bit.backend.entities.TaxEntity t : taxes) {
                if (t.getIsActive() != null && ("true".equalsIgnoreCase(t.getIsActive()) || "1".equals(t.getIsActive())
                        || "active".equalsIgnoreCase(t.getIsActive()))) {
                    try {
                        taxRate = Double.parseDouble(t.getTaxRate());
                    } catch (Exception ignored) {
                    }
                    break;
                }
            }
        } catch (Exception ignored) {
        }

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
                    if (p.getProductId() != null) {
                        item.setServiceProductId(p.getProductId());
                    } else if (p.getName() != null) {
                        java.util.Optional<com.bit.backend.entities.ProductEntity> prodOpt = productRepository
                                .findByProductName(p.getName());
                        prodOpt.ifPresent(productEntity -> item.setServiceProductId(productEntity.getId()));
                    } else {
                        java.util.Optional<com.bit.backend.entities.ProductEntity> prodOpt = productRepository
                                .findByProductName(p.getName());
                        prodOpt.ifPresent(productEntity -> item.setServiceProductId(productEntity.getId()));
                    }
                } else if ("SERVICE".equals(p.getCategory())) {
                    if (p.getServiceId() != null) {
                        item.setServiceProductId(p.getServiceId());
                    } else if (p.getName() != null) {
                        java.util.Optional<com.bit.backend.entities.ServiceEntity> servOpt = serviceRepository
                                .findByServiceName(p.getName());
                        servOpt.ifPresent(serviceEntity -> item.setServiceProductId(serviceEntity.getId()));
                    } else {
                        java.util.Optional<com.bit.backend.entities.ServiceEntity> servOpt = serviceRepository
                                .findByServiceName(p.getName());
                        servOpt.ifPresent(serviceEntity -> item.setServiceProductId(serviceEntity.getId()));
                    }
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
                entity.getPurchases().forEach(p -> {
                    p.setBilling(entity);
                    resolveBillingPurchaseIds(p);
                });
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
