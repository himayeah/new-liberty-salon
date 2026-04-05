package com.bit.backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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

}
