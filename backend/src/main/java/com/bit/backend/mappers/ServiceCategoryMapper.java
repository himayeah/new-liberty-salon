package com.bit.backend.mappers;

import com.bit.backend.dtos.ServiceCategoryDto;
import com.bit.backend.entities.ServiceCategoryEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ServiceCategoryMapper {
    ServiceCategoryDto toServiceCategoryDto(ServiceCategoryEntity serviceCategoryEntity);
    ServiceCategoryEntity toServiceCategoryEntity(ServiceCategoryDto serviceCategoryDto);
    List <ServiceCategoryDto> toServiceCategoryDtoList(List<ServiceCategoryEntity> serviceCategoryEntityList);
}
