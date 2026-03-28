package com.bit.backend.mappers;

import org.springframework.stereotype.Repository;

import com.bit.backend.dtos.SupplierDto;
import com.bit.backend.entities.SupplierEntity;

import java.util.List;
import org.mapstruct.Mapper;

@Repository
@Mapper(componentModel = "spring")
public interface SupplierMapper {

    SupplierDto toSupplierDto(SupplierEntity supplierEntity);

    SupplierEntity toSupplierEntity(SupplierDto supplierDto);

    List<SupplierDto> toSupplierDtoList(List<SupplierEntity> supplierEntityList);

}
