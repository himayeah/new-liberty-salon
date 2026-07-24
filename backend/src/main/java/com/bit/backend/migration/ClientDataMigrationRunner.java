package com.bit.backend.migration;

import com.bit.backend.entities.BillingEntity;
import com.bit.backend.entities.ClientRegEntity;
import com.bit.backend.entities.InvoiceEntity;
import com.bit.backend.repositories.BillingRepository;
import com.bit.backend.repositories.ClientRegRepository;
import com.bit.backend.repositories.InvoiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Order(1) // Run before InvoiceMigrationRunner
public class ClientDataMigrationRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ClientDataMigrationRunner.class);

    private final BillingRepository billingRepository;
    private final InvoiceRepository invoiceRepository;
    private final ClientRegRepository clientRegRepository;

    public ClientDataMigrationRunner(BillingRepository billingRepository,
                                     InvoiceRepository invoiceRepository,
                                     ClientRegRepository clientRegRepository) {
        this.billingRepository = billingRepository;
        this.invoiceRepository = invoiceRepository;
        this.clientRegRepository = clientRegRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        logger.info("Starting client data migration for billing and invoices...");

        // Migrate clients for billings
        for (BillingEntity billing : billingRepository.findAll()) {
            if (billing.getClient() == null && billing.getClientName() != null) {
                String[] names = billing.getClientName().split(" ");
                if (names.length > 1) {
                    java.util.List<ClientRegEntity> clients = clientRegRepository.findByFirstNameAndLastName(names[0], names[1]);
                    if (!clients.isEmpty()) {
                        billing.setClient(clients.get(0));
                        billingRepository.save(billing);
                        logger.info("Migrated client for billing ID: {}", billing.getId());
                    } else {
                        logger.warn("Could not find client for billing ID: {} with name: {}", billing.getId(), billing.getClientName());
                    }
                } else {
                    logger.warn("Could not parse name for billing ID: {}: {}", billing.getId(), billing.getClientName());
                }
            }
        }

        // Migrate clients for invoices
        for (InvoiceEntity invoice : invoiceRepository.findAll()) {
            if (invoice.getClient() == null && invoice.getClientName() != null) {
                String[] names = invoice.getClientName().split(" ");
                if (names.length > 1) {
                    java.util.List<ClientRegEntity> clients = clientRegRepository.findByFirstNameAndLastName(names[0], names[1]);
                    if (!clients.isEmpty()) {
                        invoice.setClient(clients.get(0));
                        invoiceRepository.save(invoice);
                        logger.info("Migrated client for invoice ID: {}", invoice.getId());
                    } else {
                        logger.warn("Could not find client for invoice ID: {} with name: {}", invoice.getId(), invoice.getClientName());
                    }
                } else {
                    logger.warn("Could not parse name for invoice ID: {}: {}", invoice.getId(), invoice.getClientName());
                }
            }
        }

        logger.info("Client data migration finished.");
    }
}
