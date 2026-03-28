package com.bit.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "service")
public class ServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_name")
    private String serviceName;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "commission")
    private BigDecimal commission;

    @Column(name = "color_code")
    private String colorCode;

    @Column(name = "description")
    private String description;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "service_category_id")
    private ServiceCategoryEntity serviceCategory;
}
