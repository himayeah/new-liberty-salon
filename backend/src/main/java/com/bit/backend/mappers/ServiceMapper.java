package com.bit.backend.mappers;

import com.bit.backend.dtos.ServiceDto;
import com.bit.backend.entities.ServiceEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper (componentModel = "spring")
public interface ServiceMapper {
    ServiceDto toServiceDto(ServiceEntity serviceEntity);
    ServiceEntity toServiceEntity(ServiceDto serviceDto);
    List<ServiceDto> toServiceDtoList(List<ServiceEntity> serviceEntityList);
}
