package com.bit.backend.services;

import com.bit.backend.dtos.ServiceDto;

import java.util.List;

public interface ServiceServiceI {
    ServiceDto addService(ServiceDto serviceDto);
    ServiceDto updateService(long id, ServiceDto serviceDto);
    List<ServiceDto> getService();
    ServiceDto deleteService(long id);

}
