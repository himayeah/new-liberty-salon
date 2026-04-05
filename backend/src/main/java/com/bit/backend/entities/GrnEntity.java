package com.bit.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "grn")
public class GrnEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id", nullable = false)
    private PurchaseOrderEntity purchaseOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(name = "ordered_date")
    private LocalDate orderedDate;

    @Column(name = "received_date")
    private LocalDate receivedDate;

    @Column(name = "ordered_quantity")
    private Double orderedQuantity;

    @Column(name = "received_quantity")
    private Double receivedQuantity;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "status")
    private String status; // PENDING, PARTIAL, RECEIVED, CANCELLED
}
