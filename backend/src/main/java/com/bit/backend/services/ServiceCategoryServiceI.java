package com.bit.backend.services;

import com.bit.backend.dtos.ServiceCategoryDto;

import java.util.List;

public interface ServiceCategoryServiceI {
    ServiceCategoryDto addServiceCategory(ServiceCategoryDto serviceCategoryDto);
    List<ServiceCategoryDto> getServiceCategory();
    ServiceCategoryDto updateServiceCategory(long id, ServiceCategoryDto serviceCategoryDto);
    ServiceCategoryDto deleteServiceCategory(long id);
}
