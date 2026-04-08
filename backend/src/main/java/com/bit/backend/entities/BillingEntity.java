package com.bit.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "biling")
public class BillingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "billing_category")
    private String billingCategory;

    @Column(name = "client_type")
    private String clientType;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "billing_date")
    private String billingDate;

    @Column(name = "payment_method")
    private String paymentMethod;

    @OneToMany(mappedBy = "billing", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private java.util.List<BillingPurchaseEntity> purchases;

}
