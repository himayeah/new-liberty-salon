package com.bit.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "invoice")
public class InvoiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invoice_number")
    private String invoiceNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private ClientRegEntity client;

    @Column(name = "client_name", insertable = false, updatable = false)
    private String clientName;

    @Column(name = "invoice_date")
    private String invoiceDate;

    @Column(name = "due_date")
    private String dueDate;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "discount_amount")
    private Double discountAmount;

    @Column(name = "tax_amount")
    private Double taxAmount;

    @Column(name = "total_amount_after_tax_and_discount")
    private Double totalAmountAfterTaxAndDiscount;

    @Column(name = "amount_paid")
    private Double amountPaid;

    @Column(name = "balance_due")
    private Double balanceDue;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billing_id")
    private BillingEntity billing;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<InvoiceItemEntity> items;
}
