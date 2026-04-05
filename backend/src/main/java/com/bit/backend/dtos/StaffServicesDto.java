package com.bit.backend.dtos;

public class StaffServicesDto {
    private Long id;
    private String serviceName;

    public StaffServicesDto(Long id, String serviceName) {
        this.id = id;
        this.serviceName = serviceName;
    }

    public StaffServicesDto(){

    }

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getServiceName() {return serviceName;}

    public void setServiceName(String serviceName) {this.serviceName = serviceName;}
}
