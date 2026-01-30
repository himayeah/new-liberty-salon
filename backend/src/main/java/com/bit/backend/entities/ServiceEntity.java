package com.bit.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name= "service")
public class ServiceEntity {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   @Column(name="service_name")
    private String serviceName;

    @Column(name="price")
    private BigDecimal price;

    @Column(name="commission")
    private BigDecimal commission;

    @Column(name="color_code")
    private String colorCode;

    @Column(name="description")
    private String description;

    @Column(name="is_Active")
    private String isActive;

    public ServiceEntity(Long id, String serviceName, BigDecimal price, BigDecimal commission, String colorCode, String description, String isActive) {
        this.id = id;
        this.serviceName = serviceName;
        this.price = price;
        this.commission = commission;
        this.colorCode = colorCode;
        this.description = description;
        this.isActive = isActive;
    }

}
