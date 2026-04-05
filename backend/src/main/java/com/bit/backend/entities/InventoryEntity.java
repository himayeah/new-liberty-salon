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
@Table(name = "inventory")
public class InventoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @Column(name = "current_stock")
    private int currentStock;

    @Column(name = "minimum_stock")
    private int minimumStock;

    @Column(name = "maximum_stock")
    private int maximumStock;

    @Column(name = "last_restocked_date")
    private LocalDate lastRestockedDate;

    @Column(name = "shelf_location")
    private String shelfLocation;
}
