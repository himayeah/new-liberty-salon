package com.bit.backend.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "staff_services")
public class StaffServicesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="service_name")
    private String serviceName;

    public StaffServicesEntity() {
    }

    public StaffServicesEntity(Long id, String serviceName) {
        this.id = id;
        this.serviceName = serviceName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
