package com.bit.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="service_category")
public class ServiceCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="category_name")
    private String categoryName;

    @Column(name="display_order")
    private Number displayOrder;

    @Column(name="description")
    private String description;

    public ServiceCategoryEntity(Long id, String categoryName, Number displayOrder, String description) {
        this.id = id;
        this.categoryName = categoryName;
        this.displayOrder = displayOrder;
        this.description = description;
    }
}
