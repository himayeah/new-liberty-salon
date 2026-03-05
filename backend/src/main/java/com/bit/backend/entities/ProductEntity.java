package com.bit.backend.entities;

import jakarta.persistence.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "product")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "brand")
    private String brand;

    @Column(name = "product_description")
    private String productDescription;

    @Column(name = "unit")
    private String unit;

    @Column(name = "purchase_price")
    private double purchasePrice;

    @Column(name = "selling_price")
    private double sellingPrice;

    @Column(name = "barcode")
    private String barcode;

    @Column(name = "sku")
    private String sku;

    @Column(name = "is_taxable")
    private boolean isTaxable;

    @Column(name = "reorder_level")
    private int reOrderLevel;

}
