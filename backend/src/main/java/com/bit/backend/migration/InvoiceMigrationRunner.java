package com.bit.backend.migration;

import com.bit.backend.entities.BillingEntity;
import com.bit.backend.entities.BillingPurchaseEntity;
import com.bit.backend.entities.InvoiceEntity;
import com.bit.backend.entities.InvoiceItemEntity;
import com.bit.backend.entities.ProductEntity;
import com.bit.backend.entities.ServiceEntity;
import com.bit.backend.entities.TaxEntity;
import com.bit.backend.repositories.BillingRepository;
import com.bit.backend.repositories.InvoiceRepository;
import com.bit.backend.repositories.ProductRepository;
import com.bit.backend.repositories.ServiceRepository;
import com.bit.backend.repositories.TaxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class InvoiceMigrationRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceMigrationRunner.class);

    private final BillingRepository billingRepository;
    private final InvoiceRepository invoiceRepository;
    private final TaxRepository taxRepository;
    private final ProductRepository productRepository;
    private final ServiceRepository serviceRepository;

    public InvoiceMigrationRunner(BillingRepository billingRepository,
            InvoiceRepository invoiceRepository,
            TaxRepository taxRepository,
            ProductRepository productRepository,
            ServiceRepository serviceRepository) {
        this.billingRepository = billingRepository;
        this.invoiceRepository = invoiceRepository;
        this.taxRepository = taxRepository;
        this.productRepository = productRepository;
        this.serviceRepository = serviceRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        logger.info("Starting one-time invoice data migration/backfill...");
        try {
            List<BillingEntity> billings = billingRepository.findAll();
            int migratedCount = 0;
            int skippedCount = 0;

            double taxRate = 0.0;
            try {
                List<TaxEntity> taxes = taxRepository.findAll();
                for (TaxEntity t : taxes) {
                    if (t.getIsActive() != null && ("true".equalsIgnoreCase(t.getIsActive())
                            || "1".equals(t.getIsActive())
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

            for (BillingEntity billing : billings) {
                Optional<InvoiceEntity> existingInvoice = invoiceRepository.findByBillingId(billing.getId());
                if (existingInvoice.isPresent()) {
                    skippedCount++;
                    continue;
                }

                logger.info("Migrating invoice for Billing ID: {}", billing.getId());
                InvoiceEntity invoice = new InvoiceEntity();
                invoice.setBilling(billing);
                invoice.setClientName(billing.getClientName());
                invoice.setInvoiceDate(billing.getBillingDate());
                invoice.setDueDate(billing.getBillingDate());
                invoice.setPaymentStatus(billing.getPaymentStatus() != null ? billing.getPaymentStatus() : "Completed");

                double totalAmount = 0.0;
                List<InvoiceItemEntity> invoiceItems = new ArrayList<>();

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
                            } else {
                                Optional<ProductEntity> prodOpt = productRepository.findByProductName(p.getName());
                                prodOpt.ifPresent(productEntity -> item.setServiceProductId(productEntity.getId()));
                            }
                        } else if ("SERVICE".equals(p.getCategory())) {
                            if (p.getServiceId() != null) {
                                item.setServiceProductId(p.getServiceId());
                            } else {
                                Optional<ServiceEntity> servOpt = serviceRepository.findByServiceName(p.getName());
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

                migratedCount++;
            }

            logger.info("Invoice migration completed successfully. Migrated: {}, Skipped: {}", migratedCount,
                    skippedCount);
        } catch (Exception e) {
            logger.error("Failed to run one-time invoice migration due to error: ", e);
        }
    }
}
